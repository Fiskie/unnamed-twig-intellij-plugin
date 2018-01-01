package com.fisk.twig.parsing

import com.fisk.twig.TwigBundle
import com.fisk.twig.parsing.TwigTokenTypes.BLOCK
import com.fisk.twig.parsing.TwigTokenTypes.COMMENT
import com.fisk.twig.parsing.TwigTokenTypes.CONTENT
import com.fisk.twig.parsing.TwigTokenTypes.EXPRESSION_CLOSE
import com.fisk.twig.parsing.TwigTokenTypes.EXPRESSION_OPEN
import com.fisk.twig.parsing.TwigTokenTypes.INVALID
import com.fisk.twig.parsing.TwigTokenTypes.BLOCK_WRAPPER
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_CLOSE
import com.fisk.twig.parsing.TwigTokenTypes.BLOCK_END_STATEMENT
import com.fisk.twig.parsing.TwigTokenTypes.INVERSE_STATEMENT
import com.fisk.twig.parsing.TwigTokenTypes.SIMPLE_STATEMENT
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_OPEN
import com.fisk.twig.parsing.TwigTokenTypes.BLOCK_START_STATEMENT
import com.fisk.twig.parsing.TwigTokenTypes.TAG
import com.fisk.twig.parsing.TwigTokenTypes.UNCLOSED_COMMENT
import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType

class TwigParsing(val builder: PsiBuilder) {
    val elseTags = setOf("else", "elseif")

    fun parse() {
        builder.setDebugMode(true)

        while (!builder.eof()) {
            parseRoot(builder)

            if (builder.eof()) {
                break
            }

            // jumped out of the parser prematurely... try and figure out what's tripping it up,
            // then jump back in

            // deal with some unexpected tokens
            val tokenType = builder.tokenType
            val problemOffset = builder.currentOffset

            if (tokenType === STATEMENT_OPEN) {
//                parseCloseBlock(builder)
            }

            if (builder.currentOffset == problemOffset) {
                // none of our error checks advanced the lexer, do it manually before we
                // try and resume parsing to avoid an infinite loop
                val problemMark = builder.mark()
                builder.advanceLexer()
                problemMark.error(TwigBundle.message("twig.parsing.invalid"))
            }
        }
    }

    fun parseRoot(builder: PsiBuilder) {
        val statementsMarker = builder.mark()

        // parse zero or more statements (empty statements are acceptable)
        while (true) {
            val optionalStatementMarker = builder.mark()
            if (parseBlock(builder)) {
                optionalStatementMarker.drop()
            } else {
                optionalStatementMarker.rollbackTo()
                break
            }
        }

        statementsMarker.done(BLOCK)
    }

    fun parseBlock(builder: PsiBuilder): Boolean {
        if (parseExpression(builder)) {
            return true
        }

        if (parseStatement(builder)) {
            return true
        }

        if (builder.tokenType == CONTENT) {
            // eat non-Twig content
            builder.advanceLexer()
            return true
        }

        if (builder.tokenType == UNCLOSED_COMMENT) {
            val unclosedCommentMarker = builder.mark()
            parseLeafToken(builder, UNCLOSED_COMMENT)
            unclosedCommentMarker.error(TwigBundle.message("twig.parsing.comment.unclosed"))
            return true
        }

        if (builder.tokenType == COMMENT) {
            parseLeafToken(builder, COMMENT)
            return true
        }

        return false
    }

    fun parseStatement(builder: PsiBuilder): Boolean {
        if (builder.tokenType == STATEMENT_OPEN) {
            // If an inverse marker is discovered in the block, we need to break out of the block
            val prematureInverseMarker = builder.mark()

            if (parseInverseBlock(builder) != null) {
                prematureInverseMarker.rollbackTo()
                return false
            }

            prematureInverseMarker.rollbackTo()

            val statementMarker = builder.mark()
            val nonStackingMarker = builder.mark()

            val openTag = parseOpenBlock(builder)

            if (openTag != null) {
                parseRoot(builder)

                while (true) {
                    // handle inverse chain
                    if (parseInverseBlock(builder) != null) {
                        parseRoot(builder)
                    } else {
                        break
                    }
                }

                if (parseCloseBlock(builder, openTag) != null) {
                    nonStackingMarker.drop()
                    statementMarker.done(BLOCK_WRAPPER)
                    return true
                }

                nonStackingMarker.rollbackTo()
                parseNonStackingBlock(builder)
                statementMarker.done(BLOCK_WRAPPER)

                return true
            }
        }

        return false
    }

    fun parseNonStackingBlock(builder: PsiBuilder): String? {
        return parseBlock(builder, SIMPLE_STATEMENT, { tag -> true })
    }

    fun parseOpenBlock(builder: PsiBuilder): String? {
        return parseBlock(builder, BLOCK_START_STATEMENT, { tag -> !isEndTag(tag) })
    }

    fun parseCloseBlock(builder: PsiBuilder, openTag: String): String? {
        return parseBlock(builder, BLOCK_END_STATEMENT, { tag -> normaliseTag(tag) == openTag })
    }

    fun parseAnyCloseBlock(builder: PsiBuilder): String? {
        return parseBlock(builder, BLOCK_END_STATEMENT, { tag -> isEndTag(tag) })
    }

    fun parseInverseBlock(builder: PsiBuilder): String? {
        // needs to be set up better for inverse tag chaining really
        return parseBlock(builder, INVERSE_STATEMENT, { tag -> elseTags.contains(tag) })
    }

    fun parseBlock(builder: PsiBuilder, type: TwigCompositeElementType, strategy: (String) -> Boolean): String? {
        val marker = builder.mark()
        var tagName: String? = null

        do {
            builder.advanceLexer()

            if (builder.tokenType == TAG) {
                val tag = builder.tokenText

                tag?.let {
                    if (strategy(tag)) {
                        tagName = normaliseTag(tag)
                    }
                }
            }

            if (builder.tokenType == STATEMENT_CLOSE) {
                break
            }
        } while (!builder.eof())

        builder.advanceLexer() // consume last STATEMENT_CLOSE

        if (tagName != null) {
            marker.done(type)
            return tagName
        } else {
            marker.rollbackTo()
            return null
        }
    }

    fun parseExpression(builder: PsiBuilder): Boolean {
        if (builder.tokenType == EXPRESSION_OPEN) {
            val expressionMarker = builder.mark()

            do {
                builder.advanceLexer()

                if (builder.tokenType == EXPRESSION_CLOSE) {
                    break
                }
            } while (!builder.eof())

            builder.advanceLexer() // consume last EXPRESSION_CLOSE

            expressionMarker.done(TwigTokenTypes.EXPRESSION)
            return true
        }

        return false
    }

    /**
     * Tries to parse the given token, marking an error if any other token is found
     */
    protected fun parseLeafToken(builder: PsiBuilder, leafTokenType: IElementType): Boolean {
        val leafTokenMark = builder.mark()
        if (builder.tokenType === leafTokenType) {
            builder.advanceLexer()
            leafTokenMark.done(leafTokenType)
            return true
        } else if (builder.tokenType === INVALID) {
            while (!builder.eof() && builder.tokenType === INVALID) {
                builder.advanceLexer()
            }
            recordLeafTokenError(INVALID, leafTokenMark)
            return false
        } else {
            recordLeafTokenError(leafTokenType, leafTokenMark)
            return false
        }
    }

    private fun recordLeafTokenError(expectedToken: IElementType, unexpectedTokensMarker: PsiBuilder.Marker) {
        if (expectedToken is TwigElementType) {
            unexpectedTokensMarker.error(expectedToken.parseExpectedMessage())
        } else {
            unexpectedTokensMarker.error(TwigBundle.message("twig.parsing.element.expected.invalid"))
        }
    }

    /**
     * Detects whether this tag is closing a block or not
     */
    private fun isEndTag(tag: String) = tag.length > 3 && tag.substring(0, 3) == "end"

    /**
     * Truncates the "end" of an end tag (e.g. endif) to return the start tag (e.g. if)
     */
    private fun normaliseTag(tag: String): String {
        return if (isEndTag(tag)) {
            tag.substring(3)
        } else {
            tag
        }
    }
}
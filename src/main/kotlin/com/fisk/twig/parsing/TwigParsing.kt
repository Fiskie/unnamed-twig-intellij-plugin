package com.fisk.twig.parsing

import com.fisk.twig.TwigBundle
import com.fisk.twig.parsing.TwigTokenTypes.CONTENT
import com.fisk.twig.parsing.TwigTokenTypes.EXPRESSION_CLOSE
import com.fisk.twig.parsing.TwigTokenTypes.EXPRESSION_OPEN
import com.fisk.twig.parsing.TwigTokenTypes.INVALID
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_BLOCK
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_CLOSE
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_CLOSE_BLOCK
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_OPEN
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_OPEN_BLOCK
import com.fisk.twig.parsing.TwigTokenTypes.TAG
import com.fisk.twig.parsing.TwigTokenTypes.BLOCK
import com.fisk.twig.parsing.TwigTokenTypes.COMMENT
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_ELSE_BLOCK
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_NON_STACKING_BLOCK
import com.fisk.twig.parsing.TwigTokenTypes.UNCLOSED_COMMENT
import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import com.sun.org.apache.xpath.internal.operations.Bool

// TODO: handle 'else', which will be used as an inverse tag for 'for' and 'if'
class TwigParsing(val builder: PsiBuilder) {
    val nonStackingTags = setOf("extends", "do", "set")
    val elseTags = setOf("else", "elseif")

    fun parse() {
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
                parseCloseBlock(builder)
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

    fun parseBlock(builder: PsiBuilder) : Boolean {
        val tokenType = builder.tokenType

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

    fun parseStatement(builder: PsiBuilder) : Boolean {
        if (builder.tokenType == STATEMENT_OPEN) {
            val statementMarker = builder.mark()

            // If this is false, must be a non-stacking statement (do, extends, etc)
            if (parseOpenBlock(builder)) {
                parseRoot(builder)

                if (parseElseBlock(builder)) {
                    parseRoot(builder)
                } else {
                    parseCloseBlock(builder)
                }

                statementMarker.done(STATEMENT_BLOCK)
                return true
            } else if (parseNonStackingBlock(builder)) {
                statementMarker.done(STATEMENT_BLOCK)
                return true
            }
        }

        return false
    }

    fun parseNonStackingBlock(builder: PsiBuilder) : Boolean {
        return parseBlock(builder, STATEMENT_NON_STACKING_BLOCK, { tag -> nonStackingTags.contains(tag) })
    }

    fun parseOpenBlock(builder: PsiBuilder) : Boolean {
        return parseBlock(builder, STATEMENT_OPEN_BLOCK, { tag -> !isEndTag(tag) && !nonStackingTags.contains(tag) })
    }

    fun parseCloseBlock(builder: PsiBuilder) : Boolean {
        return parseBlock(builder, STATEMENT_CLOSE_BLOCK, { tag -> isEndTag(tag) })
    }

    fun parseElseBlock(builder: PsiBuilder) : Boolean {
        // needs to be set up better for inverse tag chaining really
        return parseBlock(builder, STATEMENT_ELSE_BLOCK, { tag -> elseTags.contains(tag) })
    }

    fun parseBlock(builder: PsiBuilder, type: TwigCompositeElementType, strategy: (String) -> Boolean) : Boolean {
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
        } while (!(builder.eof() || builder.rawLookup(-1) == STATEMENT_CLOSE))

        if (tagName != null) {
            marker.done(type)
            return true
        } else {
            marker.rollbackTo()
            return false
        }
    }

    fun parseExpression(builder: PsiBuilder) : Boolean {
        if (builder.tokenType == EXPRESSION_OPEN) {
            val expressionMarker = builder.mark()

            do {
                builder.advanceLexer()
            } while (!(builder.eof() || builder.rawLookup(-1) == EXPRESSION_CLOSE))

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
    private fun normaliseTag(tag: String) : String {
        return if (isEndTag(tag)) {
            tag.substring(3)
        } else {
            tag
        }
    }
}
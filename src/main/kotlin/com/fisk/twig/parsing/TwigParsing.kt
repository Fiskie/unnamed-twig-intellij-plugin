package com.fisk.twig.parsing

import com.fisk.twig.TwigBundle
import com.fisk.twig.parsing.TwigTokenTypes.BLOCK
import com.fisk.twig.parsing.TwigTokenTypes.BLOCK_END_STATEMENT
import com.fisk.twig.parsing.TwigTokenTypes.BLOCK_START_STATEMENT
import com.fisk.twig.parsing.TwigTokenTypes.BLOCK_WRAPPER
import com.fisk.twig.parsing.TwigTokenTypes.BOOLEAN
import com.fisk.twig.parsing.TwigTokenTypes.COMMENT
import com.fisk.twig.parsing.TwigTokenTypes.CONTENT
import com.fisk.twig.parsing.TwigTokenTypes.EXPRESSION
import com.fisk.twig.parsing.TwigTokenTypes.EXPRESSION_CLOSE
import com.fisk.twig.parsing.TwigTokenTypes.EXPRESSION_OPEN
import com.fisk.twig.parsing.TwigTokenTypes.INVALID
import com.fisk.twig.parsing.TwigTokenTypes.INVERSE_STATEMENT
import com.fisk.twig.parsing.TwigTokenTypes.LABEL
import com.fisk.twig.parsing.TwigTokenTypes.NUMBER
import com.fisk.twig.parsing.TwigTokenTypes.SIMPLE_STATEMENT
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_CLOSE
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_OPEN
import com.fisk.twig.parsing.TwigTokenTypes.STRING
import com.fisk.twig.parsing.TwigTokenTypes.TAG
import com.fisk.twig.parsing.TwigTokenTypes.UNCLOSED_COMMENT
import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType

class TwigParsing(private val builder: PsiBuilder) {
    private val elseTags = setOf("else", "elseif")

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

            // TODO mark this error when looking for a matching close tag, so the error does not propagate down the tree
            if (tokenType === STATEMENT_OPEN) {
                val problemMark = builder.mark()

                if (parseAnyCloseStatement(builder) != null) {
                    problemMark.error(TwigBundle.message("twig.parsing.close_block_mismatch"))
                } else {
                    // not sure what this is yet
                    problemMark.drop()
                }
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

    private fun parseRoot(builder: PsiBuilder) {
        val statementsMarker = builder.mark()

        // parse zero or more statements (empty statements are acceptable)
        while (true) {
            val optionalStatementMarker = builder.mark()
            if (parseStatement(builder)) {
                optionalStatementMarker.drop()
            } else {
                optionalStatementMarker.rollbackTo()
                break
            }
        }

        statementsMarker.done(BLOCK)
    }

    private fun parseStatement(builder: PsiBuilder): Boolean {
        if (parseExpressionBlock(builder)) {
            return true
        }

        if (parseStatementChain(builder)) {
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
            // todo: not being marked at all? merge seems to not pass this token
            unclosedCommentMarker.error(TwigBundle.message("twig.parsing.comment.unclosed"))
            return true
        }

        if (builder.tokenType == COMMENT) {
            parseLeafToken(builder, COMMENT)
            return true
        }

        return false
    }

    private fun parseStatementChain(builder: PsiBuilder): Boolean {
        if (builder.tokenType == STATEMENT_OPEN) {
            // If an inverse marker is discovered in the block, we need to break out of the block
            val prematureInverseMarker = builder.mark()

            if (parseInverseStatement(builder) != null) {
                prematureInverseMarker.rollbackTo()
                return false
            }

            prematureInverseMarker.rollbackTo()

            val statementMarker = builder.mark()
            val nonStackingMarker = builder.mark()

            val openTag = parseOpenStatement(builder)

            if (openTag != null) {
                parseRoot(builder)

                while (true) {
                    // handle inverse chain
                    if (parseInverseStatement(builder) != null) {
                        parseRoot(builder)
                    } else {
                        break
                    }
                }

                if (parseCloseStatement(builder, openTag) != null) {
                    nonStackingMarker.drop()
                    statementMarker.done(BLOCK_WRAPPER)
                    return true
                }

                nonStackingMarker.rollbackTo()
                parseSingleStatement(builder)
                statementMarker.done(BLOCK_WRAPPER)

                return true
            }
        }

        return false
    }

    private fun parseSingleStatement(builder: PsiBuilder): String? {
        return parseStatement(builder, SIMPLE_STATEMENT, { _ -> true })
    }

    private fun parseOpenStatement(builder: PsiBuilder): String? {
        return parseStatement(builder, BLOCK_START_STATEMENT, { tag -> !isEndTag(tag) })
    }

    private fun parseCloseStatement(builder: PsiBuilder, openTag: String): String? {
        return parseStatement(builder, BLOCK_END_STATEMENT, { tag -> normaliseTag(tag) == openTag })
    }

    private fun parseAnyCloseStatement(builder: PsiBuilder): String? {
        return parseStatement(builder, BLOCK_END_STATEMENT, { tag -> isEndTag(tag) })
    }

    private fun parseInverseStatement(builder: PsiBuilder): String? {
        return parseStatement(builder, INVERSE_STATEMENT, { tag -> elseTags.contains(tag) })
    }

    private fun parseStatement(builder: PsiBuilder, type: TwigCompositeElementType, strategy: (String) -> Boolean): String? {
        val marker = builder.mark()
        var tagName: String? = null

        do {
            if (builder.tokenType == TAG) {
                val tag = builder.tokenText

                tag?.let {
                    if (strategy(tag)) {
                        tagName = normaliseTag(tag)
                    }
                }

                parseLeafToken(builder, TAG)
            } else {
                val expressionMarker = builder.mark()

                if (parseExpression(builder)) {
                    expressionMarker.drop()
                } else {
                    expressionMarker.rollbackTo()
                    builder.advanceLexer()
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

    private fun parseExpressionBlock(builder: PsiBuilder): Boolean {
        if (builder.tokenType == EXPRESSION_OPEN) {
            val blockMarker = builder.mark()

            do {
                builder.advanceLexer()

                val expressionMarker = builder.mark()

                if (parseExpression(builder)) {
                    expressionMarker.drop()
                } else {
                    expressionMarker.rollbackTo()
                }

                if (builder.tokenType == EXPRESSION_CLOSE) {
                    break
                }
            } while (!builder.eof())

            builder.advanceLexer() // consume last EXPRESSION_CLOSE

            blockMarker.done(TwigTokenTypes.EXPRESSION_BLOCK)
            return true
        }

        return false
    }

    /**
     * Tries to parse the given token, marking an error if any other token is found
     */
    private fun parseLeafToken(builder: PsiBuilder, leafTokenType: IElementType): Boolean {
        val leafTokenMark = builder.mark()

        when {
            builder.tokenType === leafTokenType -> {
                builder.advanceLexer()
                leafTokenMark.done(leafTokenType)
                return true
            }
            builder.tokenType === INVALID -> {
                while (!builder.eof() && builder.tokenType === INVALID) {
                    builder.advanceLexer()
                }
                recordLeafTokenError(INVALID, leafTokenMark)
                return false
            }
            else -> {
                recordLeafTokenError(leafTokenType, leafTokenMark)
                return false
            }
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

    /**
     * Parses an expression. An expression can be as simple as a single label (e.g. the foo in {{ foo }}),
     * or as complex as "foo" ~ func(bar) ~ baz.val['arr']|default("str")
     *
     * TODO: operators
     * TODO: array access
     * TODO: filter pipe
     * TODO: property access
     * TODO: functions
     */
    private fun parseExpression(builder: PsiBuilder): Boolean {
        val expressionMarker = builder.mark()

        val variableMarker = builder.mark()
        if (parseLeafToken(builder, LABEL)) {
            variableMarker.drop()

            expressionMarker.done(EXPRESSION)

//            val funMarker = builder.mark()
//
//            val sepMarker = builder.mark()

            return true
        } else {
            variableMarker.rollbackTo()
        }

        val stringMarker = builder.mark()
        if (parseLeafToken(builder, STRING)) {
            stringMarker.drop()
            expressionMarker.done(EXPRESSION)
            return true
        } else {
            stringMarker.rollbackTo()
        }

        val integerMarker = builder.mark()
        if (parseLeafToken(builder, NUMBER)) {
            integerMarker.drop()
            expressionMarker.done(EXPRESSION)
            return true
        } else {
            integerMarker.rollbackTo()
        }

        val booleanMarker = builder.mark()
        if (parseLeafToken(builder, BOOLEAN)) {
            booleanMarker.drop()
            expressionMarker.done(EXPRESSION)
            return true
        } else {
            booleanMarker.rollbackTo()
        }

        expressionMarker.error(TwigBundle.message("twig.parsing.expected.path.or.data"))
        return false
    }
}
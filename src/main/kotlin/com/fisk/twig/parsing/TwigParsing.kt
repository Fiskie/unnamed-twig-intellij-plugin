package com.fisk.twig.parsing

import com.fisk.twig.TwigBundle
import com.fisk.twig.TwigTagUtils
import com.fisk.twig.parsing.TwigTokenTypes.BLOCK
import com.fisk.twig.parsing.TwigTokenTypes.BLOCK_END_STATEMENT
import com.fisk.twig.parsing.TwigTokenTypes.BLOCK_START_STATEMENT
import com.fisk.twig.parsing.TwigTokenTypes.BLOCK_WRAPPER
import com.fisk.twig.parsing.TwigTokenTypes.BOOLEAN
import com.fisk.twig.parsing.TwigTokenTypes.COLON
import com.fisk.twig.parsing.TwigTokenTypes.COMMA
import com.fisk.twig.parsing.TwigTokenTypes.COMMENT
import com.fisk.twig.parsing.TwigTokenTypes.CONTENT
import com.fisk.twig.parsing.TwigTokenTypes.EXPRESSION
import com.fisk.twig.parsing.TwigTokenTypes.EXPRESSION_CLOSE
import com.fisk.twig.parsing.TwigTokenTypes.EXPRESSION_OPEN
import com.fisk.twig.parsing.TwigTokenTypes.FILTER_SEP
import com.fisk.twig.parsing.TwigTokenTypes.INVALID
import com.fisk.twig.parsing.TwigTokenTypes.INVERSE_STATEMENT
import com.fisk.twig.parsing.TwigTokenTypes.KEYWORD_OPERATOR
import com.fisk.twig.parsing.TwigTokenTypes.LABEL
import com.fisk.twig.parsing.TwigTokenTypes.LBRACE
import com.fisk.twig.parsing.TwigTokenTypes.LBRACKET
import com.fisk.twig.parsing.TwigTokenTypes.LPARENTH
import com.fisk.twig.parsing.TwigTokenTypes.NUMBER
import com.fisk.twig.parsing.TwigTokenTypes.OPERATOR
import com.fisk.twig.parsing.TwigTokenTypes.PROPERTY
import com.fisk.twig.parsing.TwigTokenTypes.RBRACE
import com.fisk.twig.parsing.TwigTokenTypes.RBRACKET
import com.fisk.twig.parsing.TwigTokenTypes.RPARENTH
import com.fisk.twig.parsing.TwigTokenTypes.SEP
import com.fisk.twig.parsing.TwigTokenTypes.SIMPLE_STATEMENT
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_CLOSE
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_OPEN
import com.fisk.twig.parsing.TwigTokenTypes.STRING
import com.fisk.twig.parsing.TwigTokenTypes.TAG
import com.fisk.twig.parsing.TwigTokenTypes.VARIABLE
import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType

class TwigParsing(private val builder: PsiBuilder) {
    companion object {
        val LITERAL_OR_LABEL = setOf(LABEL, STRING, NUMBER, BOOLEAN)
        val ALLOWED_EXPR_PSI = setOf(FILTER_SEP, SEP, OPERATOR, KEYWORD_OPERATOR, LPARENTH, RPARENTH, LBRACE, RBRACE, LBRACKET, RBRACKET, COLON, COMMA)
    }

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
                val problemMark = builder.mark()

                if (parseAnyCloseStatement(builder)) {
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

            if (parseTwigConstruct(builder)) {
                optionalStatementMarker.drop()
            } else {
                optionalStatementMarker.rollbackTo()
                break
            }
        }

        statementsMarker.done(BLOCK)
    }

    private fun parseTwigConstruct(builder: PsiBuilder): Boolean {
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

        if (builder.tokenType == COMMENT) {
            parseLeafToken(builder, COMMENT)
            return true
        }

        return false
    }

    /**
     * Checking for a matching end tag will prevent building a massive PSI tree then backtracking over it if the current
     * statement really isn't a block, and just a simple expression.
     *
     * This function will look ahead to check if a matching end tag even exists.
     */
    private fun hasMatchingEndTagAhead(builder: PsiBuilder, tag: String) : Boolean {
        val start = builder.mark()

        while (!builder.eof()) {
            builder.advanceLexer()

            if (builder.tokenType == TAG && builder.tokenText == "end$tag") {
                start.rollbackTo()
                return true
            }
        }

        start.rollbackTo()
        return false
    }

    /**
     * Check to see if the next statement is inverse.
     */
    private fun nextStatementIsInverse(builder: PsiBuilder): Boolean {
        val prematureInverseMarker = builder.mark()
        builder.advanceLexer()
        val isInverse = builder.tokenType == TAG && TwigTagUtils.isInverseTag(builder.tokenText!!)
        prematureInverseMarker.rollbackTo()
        return isInverse
    }

    /**
     * Parse a statement chain.
     *
     * Will return true once a completed block statement or a simple expression is parsed.
     */
    private fun parseStatementChain(builder: PsiBuilder): Boolean {
        if (builder.tokenType != STATEMENT_OPEN) {
            return false
        }

        val tagStatementMarker = builder.mark()

        builder.advanceLexer()

        // special case -- STATEMENT_CLOSE is checked to see if we're dealing with an empty statement ( {% %} )
        // This is because we don't want to mark these as errors yet, it's a little interfering
        if (builder.tokenType != TAG && builder.tokenType != STATEMENT_CLOSE) {
            tagStatementMarker.rollbackTo()
            return false
        }

        val tagName: String? = if (builder.tokenType == TAG) {
            builder.tokenText
        } else {
            null
        }

        tagStatementMarker.rollbackTo()

        // If we have run into an inverse statement it must be an inverse for the block that is currently being parsed
        // We break out; this allows the do-while later in this function to continue
        if (nextStatementIsInverse(builder)) {
            return false
        }

        val statementMarker = builder.mark()

        if (tagName == null) {
            // do nothing
        } else if (hasMatchingEndTagAhead(builder, tagName) || TwigTagUtils.isEndTag(tagName) || TwigTagUtils.isDefaultBlockTag(tagName)) {
            val blockStatementMarker = builder.mark()
            val openTag = parseOpenStatement(builder)

            if (!openTag) {
                blockStatementMarker.drop()
                statementMarker.rollbackTo()
                return false
            }

            // We have found a statement which either opens a block or is a single statement
            do {
                parseRoot(builder)
            } while (parseInverseStatement(builder))

            val closeMarker = builder.mark()

            // Doesn't matter if the close statement is mismatched -- this is handled by the annotator to prevent error coalescence
            if (parseAnyCloseStatement(builder) || TwigTagUtils.isDefaultBlockTag(tagName)) {
                blockStatementMarker.drop()
                closeMarker.drop()
                statementMarker.done(BLOCK_WRAPPER)
                return true
            }

            closeMarker.rollbackTo()
            blockStatementMarker.rollbackTo()
        }

        parseSingleStatement(builder)
        statementMarker.done(BLOCK_WRAPPER)

        return true
    }

    /**
     * Will parse any valid statement; this is used if a start/end block statement pair cannot be matched
     */
    private fun parseSingleStatement(builder: PsiBuilder): Boolean {
        return parseStatement(builder, SIMPLE_STATEMENT, { _ -> true })
    }

    /**
     * Will parse any statement that does not contain an end tag (/^end/)
     */
    private fun parseOpenStatement(builder: PsiBuilder): Boolean {
        return parseStatement(builder, BLOCK_START_STATEMENT, { tag -> !TwigTagUtils.isEndTag(tag) /* && !elseTags.contains(tag)*/ }) // todo -- this will currently break parsing
    }

    /**
     * Will parse a statement which is an end block statement for the given openTag
     */
    private fun parseCloseStatement(builder: PsiBuilder, openTag: String): Boolean {
        return parseStatement(builder, BLOCK_END_STATEMENT, { tag -> TwigTagUtils.normaliseTag(tag) == openTag })
    }

    /**
     * Will parse any close statement; this is used to gracefully handle unexpected tag errors
     */
    private fun parseAnyCloseStatement(builder: PsiBuilder): Boolean {
        return parseStatement(builder, BLOCK_END_STATEMENT, { tag -> TwigTagUtils.isEndTag(tag) })
    }

    /**
     * Will parse inverse statements, i.e. else and elseif tags
     */
    private fun parseInverseStatement(builder: PsiBuilder): Boolean {
        return parseStatement(builder, INVERSE_STATEMENT, { tag -> TwigTagUtils.isInverseTag(tag) })
    }

    private fun parseStatement(builder: PsiBuilder, type: TwigCompositeElementType, strategy: (String) -> Boolean): Boolean {
        if (builder.tokenType != STATEMENT_OPEN) {
            return false
        }

        val marker = builder.mark()
        var tagName = ""

        if (builder.lookAhead(1) != STATEMENT_CLOSE) {
            builder.advanceLexer()
        }

        do {
            if (builder.tokenType == TAG) {
                val tag = builder.tokenText

                tag?.let {
                    tagName = tag
                }

                if (!strategy(tagName)) {
                    marker.rollbackTo()
                    return false
                }

                parseLeafToken(builder, TAG)
            } else {
                if (tagName == "block" && builder.tokenType == LABEL) {
                    // make sure block labels are never parsed as expressions
                    builder.advanceLexer()
                } else if (tagName == "macro" && builder.tokenType == LABEL) {
                    // make sure macro labels are never parsed as expressions
                    builder.advanceLexer()
                } else {
                    val expressionMarker = builder.mark()

                    if (parseExpression(builder)) {
                        expressionMarker.drop()
                    } else {
                        expressionMarker.rollbackTo()
                        builder.advanceLexer()
                    }
                }
            }

            if (builder.tokenType == STATEMENT_CLOSE) {
                break
            }
        } while (!builder.eof())

        builder.advanceLexer() // consume last STATEMENT_CLOSE

        if (strategy(tagName)) {
            marker.done(type)
            return true
        } else {
            marker.rollbackTo()
            return false
        }
    }

    private fun parseExpressionBlock(builder: PsiBuilder): Boolean {
        if (builder.tokenType != EXPRESSION_OPEN) {
            return false
        }

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
     * Starting with a LABEL, look ahead for the following:
     * dot-notation
     * parentheses
     * brackets
     *
     * the goal is to work out the context of a reference, whether it is a
     * - variable
     * - property
     * - function/method name
     *
     * This will help the reference parser identify the context of each label.
     *
     * TODO: this currently will only parse var/property
     */
    private fun parseReference(builder: PsiBuilder): Boolean {
        val varMarker = builder.mark()
        builder.advanceLexer()
        varMarker.done(VARIABLE)
        var previousTokenWasValue = false

        while (true) {
            val optionalExprMarker = builder.mark()

            if (builder.tokenType == SEP) {
                builder.advanceLexer()
                previousTokenWasValue = false
                optionalExprMarker.drop()
            } else if (!previousTokenWasValue && builder.tokenType == LABEL) {
                val propMarker = builder.mark()
                builder.advanceLexer()
                propMarker.done(PROPERTY)
                previousTokenWasValue = true
                optionalExprMarker.drop()
            } else {
                optionalExprMarker.rollbackTo()
                break
            }
        }

        return true
    }

    /**
     * Return true if the current token might be a reference, and not a simple label
     */
    private fun tokenMayBeReference(builder: PsiBuilder): Boolean {
        if (builder.tokenType != LABEL) {
            return false
        }

        // lookahead for colon: prevent hash keys from being read as expressions, these are simple labels
        if (builder.lookAhead(1) == COLON) {
            return false
        }

        return true
    }

    /**
     * Parses an expression. An expression can be as simple as a single label (e.g. the foo in {{ foo }}),
     * or as complex as "foo" ~ func(bar) ~ baz.val['arr']|default("str")
     *
     * TODO: improve depth
     */
    private fun parseExpression(builder: PsiBuilder): Boolean {
        val expressionMarker = builder.mark()
        // NB: An expression is only marked if any tokens were parsed
        var any = false
        // two values in a row isn't a valid expr, they must be tag arguments
        // so we make sure this doesn't show up as a single expr in PSI
        var previousTokenWasValue = false

        while (true) {
            val optionalExprMarker = builder.mark()

            if (tokenMayBeReference(builder)) {
                parseReference(builder)
                any = true
                previousTokenWasValue = true
                optionalExprMarker.drop()
            } else if (!previousTokenWasValue && LITERAL_OR_LABEL.contains(builder.tokenType)) {
                parseLeafToken(builder, builder.tokenType!!)
                any = true
                previousTokenWasValue = true
                optionalExprMarker.drop()
            } else if (ALLOWED_EXPR_PSI.contains(builder.tokenType)) {
                builder.advanceLexer()
                any = true
                previousTokenWasValue = false
                optionalExprMarker.drop()
            } else {
                optionalExprMarker.rollbackTo()
                break
            }
        }

        if (any) {
            expressionMarker.done(EXPRESSION)
        } else {
            expressionMarker.rollbackTo()
        }

        return any
    }
}
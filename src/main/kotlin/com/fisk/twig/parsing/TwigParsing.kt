package com.fisk.twig.parsing

import com.fisk.twig.TwigBundle
import com.fisk.twig.util.TwigTagUtil
import com.fisk.twig.parsing.TwigTokenTypes.ARRAY
import com.fisk.twig.parsing.TwigTokenTypes.ARRAY_ACCESS
import com.fisk.twig.parsing.TwigTokenTypes.BLOCK
import com.fisk.twig.parsing.TwigTokenTypes.BLOCK_END_STATEMENT
import com.fisk.twig.parsing.TwigTokenTypes.BLOCK_LABEL
import com.fisk.twig.parsing.TwigTokenTypes.BLOCK_START_STATEMENT
import com.fisk.twig.parsing.TwigTokenTypes.BLOCK_WRAPPER
import com.fisk.twig.parsing.TwigTokenTypes.BOOLEAN
import com.fisk.twig.parsing.TwigTokenTypes.COLON
import com.fisk.twig.parsing.TwigTokenTypes.COMMA
import com.fisk.twig.parsing.TwigTokenTypes.COMMENT
import com.fisk.twig.parsing.TwigTokenTypes.CONTENT
import com.fisk.twig.parsing.TwigTokenTypes.EQUALS
import com.fisk.twig.parsing.TwigTokenTypes.EXPRESSION
import com.fisk.twig.parsing.TwigTokenTypes.EXPRESSION_CLOSE
import com.fisk.twig.parsing.TwigTokenTypes.EXPRESSION_OPEN
import com.fisk.twig.parsing.TwigTokenTypes.FILTER_PIPE
import com.fisk.twig.parsing.TwigTokenTypes.FUNCTION
import com.fisk.twig.parsing.TwigTokenTypes.HASH
import com.fisk.twig.parsing.TwigTokenTypes.INVALID
import com.fisk.twig.parsing.TwigTokenTypes.INVERSE_STATEMENT
import com.fisk.twig.parsing.TwigTokenTypes.KEYWORD_OPERATOR
import com.fisk.twig.parsing.TwigTokenTypes.LABEL
import com.fisk.twig.parsing.TwigTokenTypes.LBRACE
import com.fisk.twig.parsing.TwigTokenTypes.LBRACKET
import com.fisk.twig.parsing.TwigTokenTypes.LPARENTH
import com.fisk.twig.parsing.TwigTokenTypes.MACRO_DECLARATION
import com.fisk.twig.parsing.TwigTokenTypes.METHOD
import com.fisk.twig.parsing.TwigTokenTypes.NULL
import com.fisk.twig.parsing.TwigTokenTypes.NUMBER
import com.fisk.twig.parsing.TwigTokenTypes.OPERATOR
import com.fisk.twig.parsing.TwigTokenTypes.PROPERTY
import com.fisk.twig.parsing.TwigTokenTypes.RBRACE
import com.fisk.twig.parsing.TwigTokenTypes.RBRACKET
import com.fisk.twig.parsing.TwigTokenTypes.RPARENTH
import com.fisk.twig.parsing.TwigTokenTypes.DOT
import com.fisk.twig.parsing.TwigTokenTypes.SIMPLE_STATEMENT
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_CLOSE
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_OPEN
import com.fisk.twig.parsing.TwigTokenTypes.STRING
import com.fisk.twig.parsing.TwigTokenTypes.SUBEXPRESSION
import com.fisk.twig.parsing.TwigTokenTypes.TAG
import com.fisk.twig.parsing.TwigTokenTypes.VARIABLE
import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType

class TwigParsing(private val builder: PsiBuilder) {
    companion object {
        val LITERAL_OR_LABEL = setOf(LABEL, STRING, NUMBER, BOOLEAN, NULL)
        val ALLOWED_EXPR_PSI = setOf(FILTER_PIPE, OPERATOR, KEYWORD_OPERATOR)
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
    private fun hasMatchingEndTagAhead(builder: PsiBuilder, tag: String): Boolean {
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
        val isInverse = builder.tokenType == TAG && TwigTagUtil.isInverseTag(builder.tokenText!!)
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
        } else if (hasMatchingEndTagAhead(builder, tagName) || TwigTagUtil.isEndTag(tagName) || TwigTagUtil.isDefaultBlockTag(tagName)) {
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
            if (parseAnyCloseStatement(builder) || TwigTagUtil.isDefaultBlockTag(tagName)) {
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
        return parseStatement(builder, BLOCK_START_STATEMENT, { tag -> !TwigTagUtil.isEndTag(tag) /* && !elseTags.contains(tag)*/ }) // todo -- this will currently break parsing
    }

    /**
     * Will parse any close statement; this is used to gracefully handle unexpected tag errors
     */
    private fun parseAnyCloseStatement(builder: PsiBuilder): Boolean {
        return parseStatement(builder, BLOCK_END_STATEMENT, { tag -> TwigTagUtil.isEndTag(tag) })
    }

    /**
     * Will parse inverse statements, i.e. else and elseif tags
     */
    private fun parseInverseStatement(builder: PsiBuilder): Boolean {
        return parseStatement(builder, INVERSE_STATEMENT, { tag -> TwigTagUtil.isInverseTag(tag) })
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
                when {
                    tagName == "block" && builder.tokenType == LABEL -> {
                        // make sure block labels are never parsed as expressions
                        parseBlockLabel(builder)
                    }
                    tagName == "macro" && builder.tokenType == LABEL -> {
                        parseMacroDeclaration(builder)
                    }
                    else -> {
                        val expressionMarker = builder.mark()

                        if (parseExpression(builder)) {
                            expressionMarker.drop()
                        } else {
                            expressionMarker.rollbackTo()
                            builder.advanceLexer()
                        }
                    }
                }
            }

            if (builder.tokenType == STATEMENT_CLOSE) {
                builder.advanceLexer() // consume last STATEMENT_CLOSE
                break
            }
        } while (!builder.eof())

        if (strategy(tagName)) {
            marker.done(type)
            return true
        } else {
            marker.rollbackTo()
            return false
        }
    }

    private fun parseBlockLabel(builder: PsiBuilder): Boolean {
        if (builder.tokenType != LABEL) {
            return false
        }

        val macroMarker = builder.mark()
        builder.advanceLexer()
        macroMarker.done(BLOCK_LABEL)
        return true
    }

    private fun parseMacroDeclaration(builder: PsiBuilder): Boolean {
        if (builder.tokenType != LABEL) {
            return false
        }

        val macroMarker = builder.mark()
        builder.advanceLexer()
        parseFunctionCallParams(builder)
        macroMarker.done(MACRO_DECLARATION)

        return true
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
                builder.advanceLexer() // consume last EXPRESSION_CLOSE
                break
            }
        } while (!builder.eof())

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
     */
    private fun parseReference(builder: PsiBuilder): Boolean {
        if (builder.tokenType != LABEL) {
            return false
        }

        val refMarker = builder.mark()
        builder.advanceLexer()

        if (builder.tokenType == LPARENTH) {
            parseFunctionCallParams(builder)
            refMarker.done(FUNCTION)
        } else {
            // is a simple var
            refMarker.done(VARIABLE)
        }

        var previousTokenWasValue = false

        loop@ while (true) {
            val optionalExprMarker = builder.mark()

            when {
                builder.tokenType == DOT -> {
                    builder.advanceLexer()
                    previousTokenWasValue = false
                    optionalExprMarker.drop()
                }
                !previousTokenWasValue && builder.tokenType == LABEL -> {
                    val propMarker = builder.mark()
                    builder.advanceLexer()

                    if (builder.tokenType == LPARENTH) {
                        parseFunctionCallParams(builder)
                        propMarker.done(METHOD)
                    } else {
                        // is a simple var
                        propMarker.done(PROPERTY)
                    }

                    previousTokenWasValue = true
                    optionalExprMarker.drop()
                }
                parseArrayAccessor(builder) -> {
                    optionalExprMarker.drop()
                }
                else -> {
                    optionalExprMarker.rollbackTo()
                    break@loop
                }
            }
        }

        return true
    }

    /**
     * Parse the params of a function/method call, starting from the parentheses
     *
     * Includes logic to deal with named arguments, etc.
     */
    private fun parseFunctionCallParams(builder: PsiBuilder): Boolean {
        if (builder.tokenType != LPARENTH) {
            return false
        }

        val paramsMarker = builder.mark()
        builder.advanceLexer()

        var lastWasExpr = false

        loop@ while (true) {
            when {
                builder.tokenType == LABEL && builder.lookAhead(1) == EQUALS -> {
                    // we are looking at a named argument. Skip past this for now.
                    // TODO add a highlighter for named arguments, make it nice and blue
                    builder.advanceLexer()
                    builder.advanceLexer()
                    lastWasExpr = false
                }
                builder.tokenType == RPARENTH -> {
                    builder.advanceLexer()
                    break@loop
                }
                builder.tokenType == COMMA -> {
                    builder.advanceLexer()
                    lastWasExpr = false
                }
                !lastWasExpr && parseExpression(builder) -> lastWasExpr = true
                else -> {
                    paramsMarker.rollbackTo()
                    return false
                }
            }
        }

        // not marking this
        paramsMarker.drop()

        return true
    }

    /**
     * Parse a valid array accessor (e.g. foo*[123]*)
     */
    private fun parseArrayAccessor(builder: PsiBuilder): Boolean {
        if (builder.tokenType != LBRACKET) {
            return false
        }

        val arrayAccessMarker = builder.mark()
        builder.advanceLexer() // LBRACKET
        parseExpression(builder)

        if (builder.tokenType != RBRACKET) {
            // No matching ]? This isn't valid syntax
            arrayAccessMarker.rollbackTo()
            return false
        }

        builder.advanceLexer() // RBRACKET

        arrayAccessMarker.done(ARRAY_ACCESS)
        return true
    }

    private fun parseHash(builder: PsiBuilder): Boolean {
        if (builder.tokenType != LBRACE) {
            return false
        }

        val hashMarker = builder.mark()
        builder.advanceLexer()

        loop@ while (true) {
            when {
                builder.lookAhead(1) == COLON -> {
                    // parsing a hash key, which we don't want to reference
                    // (*yet*, we could possibly set up hash keys for referencing later on in plugin development)
                    builder.advanceLexer()
                    // advance the lexer over the colon too
                    builder.advanceLexer()
                }
                builder.tokenType == RBRACE -> {
                    builder.advanceLexer() // consume closing brace
                    break@loop
                }
                builder.tokenType == COMMA -> {
                    builder.advanceLexer() // consume closing brace
                }
                !parseExpression(builder) -> {
                    hashMarker.rollbackTo()
                    return false // if an expr can't be parsed, it's likely a syntax error
                }
            }
        }

        hashMarker.done(HASH)
        return true
    }

    private fun parseArray(builder: PsiBuilder): Boolean {
        if (builder.tokenType != LBRACKET) {
            return false
        }

        val arrayMarker = builder.mark()
        builder.advanceLexer()

        loop@ while (true) {
            when {
                builder.tokenType == RBRACKET -> {
                    builder.advanceLexer() // consume closing bracket
                    break@loop
                }
                builder.tokenType == COMMA -> {
                    builder.advanceLexer()
                }
                !parseExpression(builder) -> {
                    // if an expr can't be parsed, it's likely a syntax error
                    arrayMarker.rollbackTo()
                    return false
                }
            }
        }

        arrayMarker.done(ARRAY)

        return true
    }

    private fun parseSubexpression(builder: PsiBuilder): Boolean {
        if (builder.tokenType != LPARENTH) {
            return false
        }

        val subexprMarker = builder.mark()
        builder.advanceLexer()

        // todo: is loop needed here?
        loop@ while (true) {
            when {
                builder.tokenType == RPARENTH -> {
                    builder.advanceLexer() // consume closing bracket
                    break@loop
                }
                !parseExpression(builder) -> {
                    subexprMarker.rollbackTo()
                    return false // if an expr can't be parsed, it's likely a syntax error
                }
            }
        }

        subexprMarker.done(SUBEXPRESSION)

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

        loop@ while (true) {
            val optionalExprMarker = builder.mark()

            when {
                parseReference(builder) -> {
                    any = true
                    previousTokenWasValue = true
                    optionalExprMarker.drop()
                }
                !previousTokenWasValue && LITERAL_OR_LABEL.contains(builder.tokenType) -> {
                    parseLeafToken(builder, builder.tokenType!!)
                    any = true
                    previousTokenWasValue = true
                    optionalExprMarker.drop()
                }
                parseSubexpression(builder) -> {
                    any = true
                    optionalExprMarker.drop()
                    break@loop
                }
                parseArray(builder) -> {
                    any = true
                    optionalExprMarker.drop()
                    break@loop
                }
                parseHash(builder) -> {
                    any = true
                    optionalExprMarker.drop()
                    break@loop
                }
                ALLOWED_EXPR_PSI.contains(builder.tokenType) -> {
                    builder.advanceLexer()
                    any = true
                    previousTokenWasValue = false
                    optionalExprMarker.drop()
                }
                else -> {
                    optionalExprMarker.rollbackTo()
                    break@loop
                }
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
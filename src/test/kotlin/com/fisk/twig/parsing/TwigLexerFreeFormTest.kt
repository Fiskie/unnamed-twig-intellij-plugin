package com.fisk.twig.parsing

import com.fisk.twig.parsing.TwigTokenTypes.COMMENT_CLOSE
import com.fisk.twig.parsing.TwigTokenTypes.COMMENT_CONTENT
import com.fisk.twig.parsing.TwigTokenTypes.COMMENT_OPEN
import com.fisk.twig.parsing.TwigTokenTypes.CONTENT
import com.fisk.twig.parsing.TwigTokenTypes.EXPRESSION_CLOSE
import com.fisk.twig.parsing.TwigTokenTypes.EXPRESSION_OPEN
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_CLOSE
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_OPEN
import com.fisk.twig.parsing.TwigTokenTypes.TAG
import com.fisk.twig.parsing.TwigTokenTypes.VARIABLE
import com.fisk.twig.parsing.TwigTokenTypes.WHITE_SPACE

class TwigLexerFreeFormTest : TwigLexerTest() {
    fun testUnformattedPlainExpression() {
        val result = tokenize("{{foo}}")
        result.shouldMatchTokenTypes(EXPRESSION_OPEN, VARIABLE, EXPRESSION_CLOSE)
        result.shouldMatchTokenContent("{{", "foo", "}}")
    }

    fun testUnformattedPlainStatement() {
        val result = tokenize("{%foo%}")
        result.shouldMatchTokenTypes(STATEMENT_OPEN, TAG, STATEMENT_CLOSE)
        result.shouldMatchTokenContent("{%", "foo", "%}")
    }

    fun testUnformattedPlainComment() {
        val result = tokenize("{#foo#}")
        result.shouldMatchTokenTypes(COMMENT_OPEN, COMMENT_CONTENT, COMMENT_CLOSE)
        result.shouldMatchTokenContent("{#", "foo", "#}")
    }

    fun testUnformattedPlainIfStatement() {
        val result = tokenize("{%if foo%}")
        result.shouldMatchTokenTypes(STATEMENT_OPEN, TAG, WHITE_SPACE, VARIABLE, STATEMENT_CLOSE)
        result.shouldMatchTokenContent("{%", "if", " ", "foo", "%}")
    }

    fun testPlainIfStatement() {
        val result = tokenize("{% if foo %}")
        result.shouldMatchTokenTypes(STATEMENT_OPEN, WHITE_SPACE, TAG, WHITE_SPACE, VARIABLE, WHITE_SPACE, STATEMENT_CLOSE)
        result.shouldMatchTokenContent("{%", " ", "if", " ", "foo", " ", "%}")
    }

    fun testPlainStatement() {
        val result = tokenize("{% foo %}")
        result.shouldMatchTokenTypes(STATEMENT_OPEN, WHITE_SPACE, TAG, WHITE_SPACE, STATEMENT_CLOSE)
        result.shouldMatchTokenContent("{%", " ", "foo", " ", "%}")
    }

    fun testPlainComment() {
        // unlike with statements/comments, we have no reason to tokenize whitespace
        val result = tokenize("{# foo #}")
        result.shouldMatchTokenTypes(COMMENT_OPEN, COMMENT_CONTENT, COMMENT_CLOSE)
        result.shouldMatchTokenContent("{#", " foo ", "#}")
    }

    fun testContent() {
        val result = tokenize("<div>etc</div>")
        result.shouldMatchTokenTypes(CONTENT)
        result.shouldMatchTokenContent("<div>etc</div>")
    }

    fun testPlainExpressionWithWhitespaceControl() {
        val result = tokenize("{{- foo -}}")
        result.shouldMatchTokenTypes(EXPRESSION_OPEN, WHITE_SPACE, VARIABLE, WHITE_SPACE, EXPRESSION_CLOSE)
        result.shouldMatchTokenContent("{{-", " ", "foo", " ", "-}}")
    }

    fun testPlainStatementWithWhitespaceControl() {
        val result = tokenize("{%- foo -%}")
        result.shouldMatchTokenTypes(STATEMENT_OPEN, WHITE_SPACE, TAG, WHITE_SPACE, STATEMENT_CLOSE)
        result.shouldMatchTokenContent("{%-", " ", "foo", " ", "-%}")
    }

    fun testPlainCommentWithWhitespaceControl() {
        // again, unlike with statements/comments, we have no reason to tokenize whitespace
        val result = tokenize("{#- foo -#}")
        result.shouldMatchTokenTypes(COMMENT_OPEN, COMMENT_CONTENT, COMMENT_CLOSE)
        result.shouldMatchTokenContent("{#-", "foo", "-#}")
    }

    fun testUnformattedPlainExpressionWithWhitespaceControl() {
        val result = tokenize("{{-foo-}}")
        result.shouldMatchTokenTypes(EXPRESSION_OPEN, VARIABLE, EXPRESSION_CLOSE)
        result.shouldMatchTokenContent("{{-", "foo", "-}}")
    }

    fun testUnformattedPlainStatementWithWhitespaceControl() {
        val result = tokenize("{%-foo-%}")
        result.shouldMatchTokenTypes(STATEMENT_OPEN, TAG, STATEMENT_CLOSE)
        result.shouldMatchTokenContent("{%-", "foo", "-%}")
    }

    fun testUnformattedPlainCommentWithWhitespaceControl() {
        val result = tokenize("{#-foo-#}")
        result.shouldMatchTokenTypes(COMMENT_OPEN, COMMENT_CONTENT, COMMENT_CLOSE)
        result.shouldMatchTokenContent("{#-", "foo", "-#}")
    }
}
package com.fisk.twig.parsing

import com.fisk.twig.parsing.TwigTokenTypes.RBRACKET
import com.fisk.twig.parsing.TwigTokenTypes.RPARENTH
import com.fisk.twig.parsing.TwigTokenTypes.EXPRESSION_CLOSE
import com.fisk.twig.parsing.TwigTokenTypes.EXPRESSION_OPEN
import com.fisk.twig.parsing.TwigTokenTypes.FILTER_SEP
import com.fisk.twig.parsing.TwigTokenTypes.NUMBER
import com.fisk.twig.parsing.TwigTokenTypes.LBRACKET
import com.fisk.twig.parsing.TwigTokenTypes.LPARENTH
import com.fisk.twig.parsing.TwigTokenTypes.OPERATOR
import com.fisk.twig.parsing.TwigTokenTypes.SEP
import com.fisk.twig.parsing.TwigTokenTypes.STRING
import com.fisk.twig.parsing.TwigTokenTypes.VARIABLE
import com.fisk.twig.parsing.TwigTokenTypes.WHITE_SPACE

class TwigExpressionLexerTest : TwigLexerTest() {
    fun testVariableExpression() {
        val result = tokenize("{{ foo }}")
        result.shouldMatchTokenTypes(EXPRESSION_OPEN, WHITE_SPACE, VARIABLE, WHITE_SPACE, EXPRESSION_CLOSE)
        result.shouldMatchTokenContent("{{", " ", "foo", " ", "}}")
    }

    fun testConcatExpression() {
        val result = tokenize("{{ foo ~ bar }}")
        result.shouldMatchTokenTypes(EXPRESSION_OPEN, WHITE_SPACE, VARIABLE, WHITE_SPACE, OPERATOR, WHITE_SPACE, VARIABLE, WHITE_SPACE, EXPRESSION_CLOSE)
        result.shouldMatchTokenContent("{{", " ", "foo", " ", "~", " ", "bar", " ", "}}")
    }

    fun testVariableFilterExpression() {
        val result = tokenize("{{ foo|default(123) }}")
        result.shouldMatchTokenContent("{{", " ", "foo", "|", "default", "(", "123", ")", " ", "}}")
        result.shouldMatchTokenTypes(EXPRESSION_OPEN, WHITE_SPACE, VARIABLE, FILTER_SEP, VARIABLE, LPARENTH, NUMBER, RPARENTH, WHITE_SPACE, EXPRESSION_CLOSE)
    }

    fun testFunctionExpression() {
        val result = tokenize("{{ default(123) }}")
        result.shouldMatchTokenContent("{{", " ", "default", "(", "123", ")", " ", "}}")
        result.shouldMatchTokenTypes(EXPRESSION_OPEN, WHITE_SPACE, VARIABLE, LPARENTH, NUMBER, RPARENTH, WHITE_SPACE, EXPRESSION_CLOSE)
    }

    fun testStringLiteralExpression() {
        val result = tokenize("{{ 'foo' }}")
        result.shouldMatchTokenTypes(EXPRESSION_OPEN, WHITE_SPACE, STRING, WHITE_SPACE, EXPRESSION_CLOSE)
        result.shouldMatchTokenContent("{{", " ", "'foo'", " ", "}}")
    }

    fun testPropertyExpression() {
        val result = tokenize("{{ foo.bar }}")
        result.shouldMatchTokenTypes(EXPRESSION_OPEN, WHITE_SPACE, VARIABLE, SEP, VARIABLE, WHITE_SPACE, EXPRESSION_CLOSE)
        result.shouldMatchTokenContent("{{", " ", "foo", ".", "bar", " ", "}}")
    }

    fun testArrayExpression() {
        val result = tokenize("{{ foo['bar'] }}")
        result.shouldMatchTokenTypes(EXPRESSION_OPEN, WHITE_SPACE, VARIABLE, LBRACKET, STRING, RBRACKET, WHITE_SPACE, EXPRESSION_CLOSE)
        result.shouldMatchTokenContent("{{", " ", "foo", "[", "'bar'", "]",  " ", "}}")
    }

    fun testMixedPropertyArrayExpression() {
        val result = tokenize("{{ foo.bar['baz'] }}")
        result.shouldMatchTokenTypes(EXPRESSION_OPEN, WHITE_SPACE, VARIABLE, SEP, VARIABLE, LBRACKET, STRING, RBRACKET, WHITE_SPACE, EXPRESSION_CLOSE)
        result.shouldMatchTokenContent("{{", " ", "foo", ".", "bar", "[", "'baz'", "]", " ", "}}")
    }

    fun testNumberExpression() {
        val result = tokenize("{{ 123 }}")
        result.shouldMatchTokenTypes(EXPRESSION_OPEN, WHITE_SPACE, NUMBER, WHITE_SPACE, EXPRESSION_CLOSE)
        result.shouldMatchTokenContent("{{", " ", "123", " ", "}}")
    }

    fun testUnformattedVariableExpression() {
        val result = tokenize("{{foo}}")
        result.shouldMatchTokenTypes(EXPRESSION_OPEN, VARIABLE, EXPRESSION_CLOSE)
        result.shouldMatchTokenContent("{{", "foo", "}}")
    }

    fun testVariableExpressionWithWhitespaceControl() {
        val result = tokenize("{{- foo -}}")
        result.shouldMatchTokenTypes(EXPRESSION_OPEN, WHITE_SPACE, VARIABLE, WHITE_SPACE, EXPRESSION_CLOSE)
        result.shouldMatchTokenContent("{{-", " ", "foo", " ", "-}}")
    }

    fun testUnformattedVariableExpressionWithWhitespaceControl() {
        val result = tokenize("{{-foo-}}")
        result.shouldMatchTokenTypes(EXPRESSION_OPEN, VARIABLE, EXPRESSION_CLOSE)
        result.shouldMatchTokenContent("{{-", "foo", "-}}")
    }
}
package com.fisk.twig.parsing

import com.fisk.twig.parsing.TwigTokenTypes.BOOLEAN
import com.fisk.twig.parsing.TwigTokenTypes.EQUALS
import com.fisk.twig.parsing.TwigTokenTypes.LABEL
import com.fisk.twig.parsing.TwigTokenTypes.NUMBER
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_CLOSE
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_OPEN
import com.fisk.twig.parsing.TwigTokenTypes.STRING
import com.fisk.twig.parsing.TwigTokenTypes.TAG
import com.fisk.twig.parsing.TwigTokenTypes.WHITE_SPACE

class TwigStatementLexerTest : TwigLexerTest() {
    fun testUnformattedPlainStatement() {
        val result = tokenize("{%foo%}")
        result.shouldMatchTokenTypes(STATEMENT_OPEN, TAG, STATEMENT_CLOSE)
        result.shouldMatchTokenContent("{%", "foo", "%}")
    }

    fun testUnformattedPlainIfStatement() {
        val result = tokenize("{%if foo%}")
        result.shouldMatchTokenTypes(STATEMENT_OPEN, TAG, WHITE_SPACE, LABEL, STATEMENT_CLOSE)
        result.shouldMatchTokenContent("{%", "if", " ", "foo", "%}")
    }

    fun testPlainIfStatement() {
        val result = tokenize("{% if foo %}")
        result.shouldMatchTokenTypes(STATEMENT_OPEN, WHITE_SPACE, TAG, WHITE_SPACE, LABEL, WHITE_SPACE, STATEMENT_CLOSE)
        result.shouldMatchTokenContent("{%", " ", "if", " ", "foo", " ", "%}")
    }

    fun testPlainSetNumberStatement() {
        val result = tokenize("{% set foo = 123 %}")
        result.shouldMatchTokenTypes(STATEMENT_OPEN, WHITE_SPACE, TAG, WHITE_SPACE, LABEL, WHITE_SPACE, EQUALS, WHITE_SPACE, NUMBER, WHITE_SPACE, STATEMENT_CLOSE)
        result.shouldMatchTokenContent("{%", " ", "set", " ", "foo", " ", "=", " ", "123", " ", "%}")
    }

    fun testPlainSetBooleanStatement() {
        val result = tokenize("{% set foo = true %}")
        result.shouldMatchTokenTypes(STATEMENT_OPEN, WHITE_SPACE, TAG, WHITE_SPACE, LABEL, WHITE_SPACE, EQUALS, WHITE_SPACE, BOOLEAN, WHITE_SPACE, STATEMENT_CLOSE)
        result.shouldMatchTokenContent("{%", " ", "set", " ", "foo", " ", "=", " ", "true", " ", "%}")
    }

    fun testPlainSetVarStatement() {
        val result = tokenize("{% set foo = bar %}")
        result.shouldMatchTokenTypes(STATEMENT_OPEN, WHITE_SPACE, TAG, WHITE_SPACE, LABEL, WHITE_SPACE, EQUALS, WHITE_SPACE, LABEL, WHITE_SPACE, STATEMENT_CLOSE)
        result.shouldMatchTokenContent("{%", " ", "set", " ", "foo", " ", "=", " ", "bar", " ", "%}")
    }

    fun testPlainSetStringStatement() {
        val result = tokenize("{% set foo = '123' %}")
        result.shouldMatchTokenTypes(STATEMENT_OPEN, WHITE_SPACE, TAG, WHITE_SPACE, LABEL, WHITE_SPACE, EQUALS, WHITE_SPACE, STRING, WHITE_SPACE, STATEMENT_CLOSE)
        result.shouldMatchTokenContent("{%", " ", "set", " ", "foo", " ", "=", " ", "'123'", " ", "%}")
    }

    fun testPlainStatement() {
        val result = tokenize("{% foo %}")
        result.shouldMatchTokenTypes(STATEMENT_OPEN, WHITE_SPACE, TAG, WHITE_SPACE, STATEMENT_CLOSE)
        result.shouldMatchTokenContent("{%", " ", "foo", " ", "%}")
    }

    fun testPlainStatementWithWhitespaceControl() {
        val result = tokenize("{%- foo -%}")
        result.shouldMatchTokenTypes(STATEMENT_OPEN, WHITE_SPACE, TAG, WHITE_SPACE, STATEMENT_CLOSE)
        result.shouldMatchTokenContent("{%-", " ", "foo", " ", "-%}")
    }

    fun testUnformattedPlainStatementWithWhitespaceControl() {
        val result = tokenize("{%-foo-%}")
        result.shouldMatchTokenTypes(STATEMENT_OPEN, TAG, STATEMENT_CLOSE)
        result.shouldMatchTokenContent("{%-", "foo", "-%}")
    }
}
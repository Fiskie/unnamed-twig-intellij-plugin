package com.fisk.twig.parsing

import com.fisk.twig.parsing.TwigTokenTypes.CONTENT

class TwigContentLexerTest : TwigLexerTest() {
    fun testContent() {
        val result = tokenize("<div>etc</div>")
        result.shouldMatchTokenTypes(CONTENT)
        result.shouldMatchTokenContent("<div>etc</div>")
    }
}
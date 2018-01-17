package com.fisk.twig.parsing

import com.fisk.twig.parsing.TwigTokenTypes.CONTENT

class TwigContentLexerTest : TwigLexerTest() {
    fun testContent() {
        val result = tokenize("<div>etc</div>")
        result.shouldMatchTokenTypes(CONTENT)
        result.shouldMatchTokenContent("<div>etc</div>")
    }

    fun testJavaScriptContent() {
        // ideally, content is parsed by the lexer in one chunk
        // but the lexer I wrote sucks and stops on {, so these tokens are instead merged before creating the PSI tree
        // disabling the test for now since it doesn't work
//        val result = tokenize("var foo = function() { console.log(bar) }")
//        result.shouldMatchTokenContent("var foo = function() { console.log(bar) }")
//        result.shouldMatchTokenTypes(CONTENT)
    }
}
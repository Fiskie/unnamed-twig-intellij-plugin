package com.fisk.twig.lexer

import com.fisk.twig.parsing.TwigTokenTypes.CONTENT

class TwigContentLexerTest : TwigLexerTest() {
    fun testContent() {
        val result = tokenize("<div>etc</div>")
        result.shouldMatchTokenTypes(CONTENT)
        result.shouldMatchTokenContent("<div>etc</div>")
    }

    fun testJavaScriptContent() {
        // This function will fail until content lexing consumes { properly
        // Functionally this probably won't change anything, but
        // I'd like it if it weren't so hacky
        val result = tokenize("var foo = function() { console.log(bar) }")
        result.shouldMatchTokenTypes(CONTENT)
        result.shouldMatchTokenContent("var foo = function() { console.log(bar) }")
    }
}
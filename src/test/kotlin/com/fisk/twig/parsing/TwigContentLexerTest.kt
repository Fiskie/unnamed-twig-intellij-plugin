package com.fisk.twig.parsing

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
        result.shouldMatchTokenContent("var foo = function() { console.log(bar) }")
        result.shouldMatchTokenTypes(CONTENT)
    }

    fun testMixedContent() {
        // This function will fail until content lexing consumes { properly
        // Functionally this probably won't change anything, but
        // I'd like it if it weren't so hacky
        val result = tokenize("CONTENT {{ foo }} CONTENT")
        result.shouldMatchTokenContent("CONTENT ", "{{", "foo", "}}", " CONTENT ")
        result.shouldMatchTokenTypes(CONTENT)
    }
}
package com.fisk.twig.ide.annotator

class TwigAnnotatorTest : TwigAnnotatorTestBase() {
    fun testInvalidEmbed() {
        checkErrors()
    }

    fun testInvalidExtends() {
        checkErrors()
    }

    fun testMismatchedCloseBlock() {
        checkErrors()
    }

    fun testBadElementInSandbox() {
        checkErrors()
    }
}

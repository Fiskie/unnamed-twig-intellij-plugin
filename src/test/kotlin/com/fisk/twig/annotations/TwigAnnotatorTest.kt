package com.fisk.twig.annotations

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

    fun testUnexpectedInverseTag() {
        checkErrors()
    }
}

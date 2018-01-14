package com.fisk.twig.annotations

import com.fisk.twig.TwigFixtureTestBase

abstract class TwigAnnotatorTestBase : TwigFixtureTestBase() {
    protected fun checkInfo() {
        myFixture.configureByFile("annotator/${fileName}")
        myFixture.testHighlighting(false, true, false)
    }

    protected fun checkWarnings() {
        myFixture.configureByFile("annotator/${fileName}")
        myFixture.testHighlighting(true, false, true)
    }

    protected fun checkErrors() {
        myFixture.configureByFile("annotator/${fileName}")
        myFixture.testHighlighting(false, false, false)
    }
}
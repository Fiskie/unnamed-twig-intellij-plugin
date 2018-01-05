package com.fisk.twig.completion

import com.fisk.twig.file.TwigFileType
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.testFramework.UsefulTestCase
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

class TwigKeywordCompletionTest : LightPlatformCodeInsightFixtureTestCase() {
    fun doBasicTest(text: String, vararg expected: String) {
        myFixture.configureByText(TwigFileType.INSTANCE, text)
        myFixture.complete(CompletionType.BASIC)
        UsefulTestCase.assertContainsElements(myFixture.lookupElementStrings!!, *expected)
    }

    fun testSimple() {
        doBasicTest("{% <caret> %}", "if", "for")
    }

    fun testWithWhitespaceControl() {
        doBasicTest("{%- <caret> -%}", "if", "for")
    }
}
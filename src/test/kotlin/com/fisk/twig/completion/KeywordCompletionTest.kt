package com.fisk.twig.completion

import com.fisk.twig.file.TwigFileType
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.testFramework.UsefulTestCase
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

abstract class KeywordCompletionTest : LightPlatformCodeInsightFixtureTestCase() {
    fun doBasicTest(text: String, vararg expected: String) {
        doTest(text, {
            UsefulTestCase.assertContainsElements(myFixture.lookupElementStrings!!, *expected)
        })
    }

    fun doTest(text: String, assertions: (CodeInsightTestFixture) -> Unit) {
        myFixture.configureByText(TwigFileType.INSTANCE, text)
        myFixture.complete(CompletionType.BASIC)
        assertions(myFixture)
    }
}
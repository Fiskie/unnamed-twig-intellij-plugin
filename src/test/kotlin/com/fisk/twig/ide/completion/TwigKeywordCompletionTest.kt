package com.fisk.twig.ide.completion

import com.intellij.testFramework.UsefulTestCase
import junit.framework.Assert

class TwigKeywordCompletionTest : KeywordCompletionTest() {
    fun testSimple() {
        doBasicTest("{% <caret> %}", "if", "for")
    }

    fun testWithWhitespaceControl() {
        doBasicTest("{%- <caret> -%}", "if", "for")
    }

    fun testSuggestCloser() {
        doBasicTest("{% if foo %}{% <caret> %}", "endif")
    }

    fun testSuggestCloserForCustomTags() {
        doBasicTest("{% cache foo %}{% <caret> %}", "endcache")
    }

    fun testSuppressCompletedEndTags() {
        doTest("{% if foo %}{% endif %}{% <caret> %}", {
            UsefulTestCase.assertDoesntContain(it.lookupElementStrings!!, "endif")
        })

        doTest("{% if foo %}{% endif %}{% for foo in bar %}{% <caret> %}", {
            UsefulTestCase.assertContainsElements(it.lookupElementStrings!!, "endfor")
            UsefulTestCase.assertDoesntContain(it.lookupElementStrings!!, "endif")
        })
    }

    fun testNoDuplicateSuggestions() {
        doTest("{% if foo %}{% if foo %}{% if foo %}{% <caret> %}", {
            val list = it.lookupElementStrings!!
            Assert.assertEquals(list.size, list.toSet().size)
        })
    }
}
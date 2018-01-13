package com.fisk.twig.completion

import com.intellij.testFramework.UsefulTestCase

class TwigLoopVariableCompletionTest : KeywordCompletionTest() {
    fun testDoNotSuggestOutOfLoopContext() {
        doTest("{% if foo %}{{ l<caret> }}", {
            UsefulTestCase.assertDoesntContain(it.lookupElementStrings!!, "loop.index")
        })
    }

    fun testSuggestLoopWhenInContext1() {
        doBasicTest("{% for var in foo %}{{ l<caret> }}{% endfor %}", "loop.index")
    }

    fun testSuggestLoopWhenInContext2() {
        doBasicTest("{% for var in foo %}{% if foo %}{{ l<caret> }}{% endif %}{% endfor %}", "loop.index")
    }
}
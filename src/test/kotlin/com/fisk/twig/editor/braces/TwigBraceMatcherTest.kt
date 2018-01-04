package com.fisk.twig.editor.braces

import com.fisk.twig.file.TwigFileType
import com.intellij.codeInsight.highlighting.BraceMatchingUtil
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

class TwigBraceMatcherTest : LightPlatformCodeInsightFixtureTestCase() {

    /**
     * Expects "fileText" to have two "&lt;brace_match&gt;" tokens, placed in front of two braces which are
     * expected to be matched by the built-in brace matching (i.e. when the caret is at one of the &lt;brace_match&gt;
     * tokens, the brace match subsystem highlights the brace at the other &lt;brace_match&gt;)
     *
     *
     * NOTE: the &lt;brace_match&gt; before you close brace should have a bit of whitespace before it to make this
     * test work correctly.  For example, have "{{/ foo <brace_match>}}" rather than "{{/foo<brace_match>}}"
    </brace_match></brace_match> */
    private fun doBraceTest(fileText: String) {
        var textForTest = fileText

        val firstBracePosition = textForTest.indexOf(ourBraceMatchIndicator)
        textForTest = textForTest.replaceFirst(ourBraceMatchIndicator.toRegex(), "") // remove first brace from input

        val secondBracePosition = textForTest.indexOf(ourBraceMatchIndicator)
        textForTest = textForTest.replaceFirst(ourBraceMatchIndicator.toRegex(), "") // remove second brace from input

        assertTrue("Should have two \"" + ourBraceMatchIndicator + "\" tokens in fileText.  Given fileText:\n"
                + fileText,
                firstBracePosition > -1 && secondBracePosition > -1)

        val firstBraceResult = findMatchBraceForBraceAtCaret(textForTest, firstBracePosition, secondBracePosition)
        assertEquals("Result with caret at first <brace_match>", fileText, firstBraceResult)
        val secondBraceResult = findMatchBraceForBraceAtCaret(textForTest, secondBracePosition, firstBracePosition)
        assertEquals("Result with caret at second <brace_match>", fileText, secondBraceResult)
    }

    /**
     * Method to do the actual invocation of the brace match subsystem on a given file for a given caret position
     *
     * @param fileText                     the source to test brace matching on
     * @param caretPosition                caret position to compute a matched brace for
     * @param expectedMatchedBracePosition the expected position of the brace which matches the brace at caretPosition
     * @return the given file text with the {link #ourBraceMatchIndicator} tokens place where
     * the the brace matching subsystem dictated
     */
    private fun findMatchBraceForBraceAtCaret(fileText: String, caretPosition: Int, expectedMatchedBracePosition: Int): String {

        val caretIndicator = "<caret>"
        val textWithCaret = StringBuilder(fileText).insert(caretPosition, caretIndicator).toString()

        myFixture.configureByText(TwigFileType.INSTANCE, textWithCaret)

        val caretFirst = expectedMatchedBracePosition > caretPosition
        val actualBraceMatchPosition = BraceMatchingUtil.getMatchedBraceOffset(myFixture.editor,
                caretFirst,
                myFixture.file)

        // we want to have an easy to read result, so we insert a <brace_match> where
        // BraceMatchingUtil.getMatchedBraceOffset told us it should go.
        var result = StringBuilder(textWithCaret)
                // note that we need to compensate for the length of the caretIndicator if it comes before the ourBraceMatchIndicator
                .insert(actualBraceMatchPosition + if (caretFirst) caretIndicator.length else 0, ourBraceMatchIndicator)
                .toString()

        // replace the caret indicator with a ourBraceMatchIndicator so that our result format matches our input format
        result = result.replace(caretIndicator, ourBraceMatchIndicator)

        return result
    }

    fun testExpression() {
        doBraceTest(
                ourTestSource.replace("{{ bar }}", "<brace_match>{{ bar }}")
                        .replace("{{ bar }}", "{{ bar <brace_match>}}")
        )
    }

    fun testStatement() {
        doBraceTest(
                ourTestSource.replace("{% if foo %}", "<brace_match>{% if foo %}")
                        .replace("{% if foo %}", "{% if foo <brace_match>%}")
        )
    }

    companion object {

        private val ourBraceMatchIndicator = "<brace_match>"

        /**
         * Convenience property for quickly setting up brace match tests.
         *
         *
         * Things to note about this text:
         * - The braces we want to match have some whitespace around them (this lets them match when the caret is before them)
         * - All mustache ids (foo, foo2, bar, etc) are unique so that they can be easily targeted
         * by string replace functions.
         */
        private val ourTestSource = "{% if foo %}\n" +
                "    {{ bar }}\n" +
                "    {{ baz }}\n" +
                "    {% if foo2 %}\n" +
                "        <div>\n" +
                "            {% if foo3 %}\n" +
                "                Content\n" +
                "            {% endif %}\n" +
                "        </div>\n" +
                "        {{ baz2 }}\n" +
                "        {{ bat }}\n" +
                "        {{ baz3 }}\n" +
                "    {% endif %}\n" +
                "{% else %}\n" +
                "    Content\n" +
                "{% endif %}\n"
    }
}

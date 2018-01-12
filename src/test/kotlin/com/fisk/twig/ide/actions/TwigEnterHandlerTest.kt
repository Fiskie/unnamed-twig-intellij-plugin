package com.fisk.twig.ide.actions

class TwigEnterHandlerTest : TwigActionHandlerTest() {
    /**
     * On Enter between matching open/close tags,
     * expect an extra newline to be inserted with the caret placed
     * between the tags
     */
    fun testEnterBetweenMatchingTwigTags() {
        doEnterTest(
                "{% if foo %}<caret>{% endif %}",
                "{% if foo %}\n" +
                        "    <caret>\n" +
                        "{% endif %}"
        )
    }


    /**
     * On Enter between matching open/close tags,
     * expect an extra newline to be inserted with the caret placed
     * between the tags
     */
    fun testEnterBetweenMatchingTwigTagsWithWhitespaceControl() {
        doEnterTest(
                "{%- if foo -%}<caret>{%- endif -%}",
                "{%- if foo -%}\n" +
                        "    <caret>\n" +
                        "{%- endif -%}"
        )
    }

    /**
     * On Enter between MIS-matched open/close tags,
     * we still get the standard behavior
     */
    fun testEnterBetweenMismatchedTwigTags() {
        doEnterTest(
                "{% if foo %}<caret>{% endfor %}" + "stuff",
                "{% if foo %}\n" +
                        "    <caret>\n" +
                        "{% endfor %}" +
                        "stuff"
        )
    }

    /**
     * On Enter at an open tag with no close tag,
     * expect a standard newline
     * (Notice that we have "other stuff" our test string.  When the caret is at the file
     * boundary, it's actually a special case.  See [.testEnterAtOpenTagOnFileBoundary]
     */
    fun testEnterAtOpenTag() {
        doEnterTest(
                "{% if foo %}<caret>" + "other stuff",
                "{% if foo %}\n" +
                        "    <caret>other stuff"

        )
    }

    /**
     * On Enter at an open tag with no close tag,
     * expect a standard newline.
     */
    fun testEnterAtOpenTagOnFileBoundary() {
        doEnterTest(
                "{% if foo %}<caret>",
                "{% if foo %}\n" + "    <caret>"
        )
    }
}
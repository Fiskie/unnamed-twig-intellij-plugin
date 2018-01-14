package com.fisk.twig.actions

class TwigCommentActionTest : TwigActionHandlerTest() {
    fun testNativeInsertLineComment1() {
        doLineCommentTest(
                "{% if foo %}<caret>",
                "{#{% if foo %}<caret>#}"
        )
    }

    fun testNativeInsertLineComment2() {
        doLineCommentTest(
                "{% if foo %}\n" +
                        "<caret>    {{ bar }}\n" +
                        "{% endif %}",

                "{% if foo %}\n" +
                        "    {#{{ bar }}#}\n" +
                        "<caret>{% endif %}"
        )
    }

    fun testNativeInsertBlockComment1() {
        doBlockCommentTest(
                "{% if foo %}<caret>",
                "{% if foo %}{#<caret>#}"
        )
    }

    fun testNativeInsertBlockComment2() {
        doBlockCommentTest(

                "{% if foo %}\n" +
                        "    <caret>{{ bar }}\n" +
                        "{% endif %",

                "{% if foo %}\n" +
                        "    {#<caret>#}{{ bar }}\n" +
                        "{% endif %"
        )
    }

    fun testNativeInsertBlockCommentWithSelection() {
        doBlockCommentTest(

                "<selection><caret>{% if foo %}" +
                        "    {{ bar }}</selection>" +
                        "{% endif %",

                "<selection>{#<caret>{% if foo %}" +
                        "    {{ bar }}#}</selection>" +
                        "{% endif %"
        )
    }
}

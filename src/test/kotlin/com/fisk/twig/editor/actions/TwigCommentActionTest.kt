package com.fisk.twig.editor.actions

import com.fisk.twig.TwigLanguage
import com.fisk.twig.config.TwigConfig
import com.intellij.lang.Language
import com.intellij.lang.javascript.JavascriptLanguage

class TwigCommentActionTest : TwigActionHandlerTest() {

    private var myPrevCommenterLang: Language? = null

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()

        myPrevCommenterLang = TwigConfig.commenterLanguage

        // ensure that no commenter is selected to make sure that we test defaulting to HTML comments
        TwigConfig.commenterLanguage = null
    }

    fun testInsertLineComment1() {
        doLineCommentTest(
                "{% if foo %}<caret>",
                "<!--{% if foo %}<caret>-->"
        )
    }

    fun testInsertLineComment2() {
        doLineCommentTest(

                "{% if foo %}\n" +
                        "<caret>    {{ bar }}\n" +
                        "{% endif %}",

                "{% if foo %}\n" +
                        "    <!--{{ bar }}-->\n" +
                        "<caret>{% endif %}"
        )
    }

    fun testInsertBlockComment1() {
        doBlockCommentTest(
                "{% if foo %}<caret>",

                "{% if foo %}<!--<caret>-->"
        )
    }

    fun testInsertBlockComment2() {
        doBlockCommentTest(

                "{% if foo %}\n" +
                        "    <caret>{{ bar }}\n" +
                        "{% endif %",

                "{% if foo %}\n" +
                        "    <!--<caret>-->{{ bar }}\n" +
                        "{% endif %"
        )
    }

    fun testInsertBlockCommentWithSelection() {
        doBlockCommentTest(

                "<selection><caret>{% if foo %}" +
                        "    {{ bar }}</selection>" +
                        "{% endif %",

                "<selection><!--<caret>{% if foo %}" +
                        "    {{ bar }}--></selection>" +
                        "{% endif %"
        )
    }

    fun testInsertNonDefaultLineComment() {
        val prevCommenterLanguage = TwigConfig.commenterLanguage
        TwigConfig.commenterLanguage = JavascriptLanguage.INSTANCE

        doLineCommentTest(
                "{% if foo %}<caret>",
                "//{% if foo %}<caret>"
        )

        TwigConfig.commenterLanguage = prevCommenterLanguage
    }

    fun testInsertNonDefaultBlockComment() {
        val prevCommenterLanguage = TwigConfig.commenterLanguage
        TwigConfig.commenterLanguage = JavascriptLanguage.INSTANCE

        doBlockCommentTest(
                "{% if foo %}<caret>",
                "{% if foo %}/*<caret>*/"
        )

        TwigConfig.commenterLanguage = prevCommenterLanguage
    }

    fun testNativeInsertLineComment1() {
        val prevCommenterLang = TwigConfig.commenterLanguage
        TwigConfig.commenterLanguage = TwigLanguage.INSTANCE

        doLineCommentTest(
                "{% if foo %}<caret>",
                "{#{% if foo %}<caret>#}"
        )

        TwigConfig.commenterLanguage = prevCommenterLang
    }

    fun testNativeInsertLineComment2() {
        val prevCommenterLang = TwigConfig.commenterLanguage
        TwigConfig.commenterLanguage = TwigLanguage.INSTANCE

        doLineCommentTest(
                "{% if foo %}\n" +
                        "<caret>    {{ bar }}\n" +
                        "{% endif %}",

                "{% if foo %}\n" +
                        "    {#{{ bar }}#}\n" +
                        "<caret>{% endif %}"
        )

        TwigConfig.commenterLanguage = prevCommenterLang
    }

    fun testNativeInsertBlockComment1() {
        val prevCommenterLang = TwigConfig.commenterLanguage
        TwigConfig.commenterLanguage = TwigLanguage.INSTANCE

        doBlockCommentTest(
                "{% if foo %}<caret>",
                "{% if foo %}{#<caret>#}"
        )

        TwigConfig.commenterLanguage = prevCommenterLang
    }

    fun testNativeInsertBlockComment2() {
        val prevCommenterLang = TwigConfig.commenterLanguage
        TwigConfig.commenterLanguage = TwigLanguage.INSTANCE

        doBlockCommentTest(

                "{% if foo %}\n" +
                        "    <caret>{{ bar }}\n" +
                        "{% endif %",

                "{% if foo %}\n" +
                        "    {#<caret>#}{{ bar }}\n" +
                        "{% endif %"
        )

        TwigConfig.commenterLanguage = prevCommenterLang
    }

    fun testNativeInsertBlockCommentWithSelection() {
        val prevCommenterLang = TwigConfig.commenterLanguage
        TwigConfig.commenterLanguage = TwigLanguage.INSTANCE

        doBlockCommentTest(

                "<selection><caret>{% if foo %}" +
                        "    {{ bar }}</selection>" +
                        "{% endif %",

                "<selection>{#<caret>{% if foo %}" +
                        "    {{ bar }}#}</selection>" +
                        "{% endif %"
        )

        TwigConfig.commenterLanguage = prevCommenterLang
    }

    @Throws(Exception::class)
    override fun tearDown() {
        TwigConfig.commenterLanguage = myPrevCommenterLang
        super.tearDown()
    }
}

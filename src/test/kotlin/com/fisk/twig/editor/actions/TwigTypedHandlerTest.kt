package com.fisk.twig.editor.actions

import com.fisk.twig.config.TwigConfig
import com.fisk.twig.format.FormatterTestSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsManager

class TwigTypedHandlerTest : TwigActionHandlerTest() {
    private var myPrevAutoCloseSetting: Boolean = false
    private var formatterTestSettings: FormatterTestSettings? = null

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()

        myPrevAutoCloseSetting = TwigConfig.isAutoGenerateCloseTagEnabled
        TwigConfig.isAutoGenerateCloseTagEnabled = true

        formatterTestSettings = FormatterTestSettings(CodeStyleSettingsManager.getSettings(project))
        formatterTestSettings!!.setUp()
    }

    @Throws(Exception::class)
    override fun tearDown() {
        TwigConfig.isAutoGenerateCloseTagEnabled = myPrevAutoCloseSetting
        formatterTestSettings!!.tearDown()

        super.tearDown()
    }

    fun testStatementBraceAutocomplete() {
        TwigConfig.isAutocompleteEndBracesEnabled = false
        doCharTest('%', "foo {<caret>", "foo {%<caret>")

        TwigConfig.isAutocompleteEndBracesEnabled = true
        doCharTest('%', "foo {<caret>", "foo {% <caret> %}")

//        TwigConfig.isAutoGenerateCloseTagEnabled = true
//        doCharTest('}', "foo {% if bar %<caret>", "foo {% if bar %}<caret>{% endif %}")
//        doCharTest('}', "foo {% if bar %}{% if baz %<caret>{% endif %}", "foo {% if bar %}{% if baz %}<caret>{% endif %}{% endif %}")
//
//        TwigConfig.isAutoGenerateCloseTagEnabled = false
//        doCharTest('}', "foo {% if bar %<caret>", "foo {% if bar %}}<caret>")
//        doCharTest('}', "foo {% if bar %}{% if baz %<caret>{% endif %}", "foo {% if bar %}{% if baz %}<caret>{% endif %}")
    }

    fun testExpressionBraceAutocomplete() {
        TwigConfig.isAutocompleteEndBracesEnabled = false
        doCharTest('{', "foo {<caret>", "foo {{<caret>")

        TwigConfig.isAutocompleteEndBracesEnabled = true
        doCharTest('{', "foo {<caret>", "foo {{ <caret> }}")
    }

    fun testCommentBraceAutocomplete() {
        TwigConfig.isAutocompleteEndBracesEnabled = false
        doCharTest('#', "foo {<caret>", "foo {#<caret>")

        TwigConfig.isAutocompleteEndBracesEnabled = true
        doCharTest('#', "foo {<caret>", "foo {#<caret>#}")

    }

    fun testWhitespaceControlAutocomplete() {
        TwigConfig.isAutocompleteEndBracesEnabled = false
        doCharTest('-', "foo {{<caret> bar }}", "foo {{-<caret> bar }}")

        TwigConfig.isAutocompleteEndBracesEnabled = true
        doCharTest('-', "foo {{<caret> bar }}", "foo {{-<caret> bar -}}")
    }

    fun testInsertCloseTag() {
        TwigConfig.isAutoGenerateCloseTagEnabled = true
        doCharTest('}', "{% if bar %<caret>", "{% if bar %}<caret>{% endif %}")
    }

    fun testInsertCloseTagForNestedBlocks() {
        TwigConfig.isAutoGenerateCloseTagEnabled = true
        doCharTest('}', "{% if foo %}{% if bar %<caret>{% endif %}", "{% if foo %}{% if bar %}<caret>{% endif %}{% endif %}")
    }

    fun testInsertCloseTagWithWhitespace() {
        // ensure that we properly identify the "foo" even if there's whitespace between it and the open tag
        TwigConfig.isAutoGenerateCloseTagEnabled = true
        doCharTest('}', "{%   if foo   %<caret>", "{%   if foo   %}<caret>{% endif %}")
    }

    fun testInsertCloseTagForComplexExpressions() {
        TwigConfig.isAutocompleteEndBracesEnabled = true
        doCharTest('}', "{{ foo.bar|baz|cat() <caret>", "{{ foo.bar|baz|cat() }}<caret>")
        doCharTest('}', "{{ foo.bar + baz.cat <caret>", "{{ foo.bar + baz.cat }}<caret>")
    }

    fun testNoInsertCloseTagForExtraBraces() {
        TwigConfig.isAutocompleteEndBracesEnabled = true
        doCharTest('}', "{{ foo }}<caret>", "{{ foo }}}<caret>")
    }

    fun testRegularStache() {
        // ensure that nothing special happens for regular 'staches, whether autoGenerateCloseTag is enabled or not

        TwigConfig.isAutocompleteEndBracesEnabled = true
        doCharTest('}', "{{ foo }<caret>", "{{ foo }}<caret>")
        doCharTest('}', "{{ foo|bar|baz }<caret>", "{{ foo|bar|baz }}<caret>")

        // test when caret is not at file boundary
        TwigConfig.isAutocompleteEndBracesEnabled = true
        doCharTest('}', "{{ foo }<caret>some\nother stuff", "{{ foo }}<caret>some\nother stuff")
        doCharTest('}', "{{ foo|bar|baz }<caret>some\nother stuff", "{{ foo|bar|baz }}<caret>some\nother stuff")

        TwigConfig.isAutocompleteEndBracesEnabled = false
        doCharTest('}', "{{ foo }<caret>", "{{ foo }}<caret>")
        doCharTest('}', "{{ foo|bar|baz }<caret>", "{{ foo|bar|baz }}<caret>")
    }

    /**
     * Our typed handler relies on looking a couple of characters back
     * make sure we're well behaved when there are none.
     */
    fun testFirstCharTyped() {
        TwigConfig.isAutocompleteEndBracesEnabled = true
        doCharTest('}', "<caret>", "}<caret>")

        TwigConfig.isAutocompleteEndBracesEnabled = false
        doCharTest('}', "<caret>", "}<caret>")
    }

    /**
     * Ensure that IDEA does not provide any automatic "}" insertion
     */
    fun testSuppressNativeBracketInsert() {
        TwigConfig.isAutoGenerateCloseTagEnabled = true
        doCharTest('{', "<caret>", "{<caret>")
        doCharTest('{', "{<caret>", "{{<caret>")
        doCharTest('{', "{{<caret>", "{{{<caret>")

        TwigConfig.isAutoGenerateCloseTagEnabled = false
        doCharTest('{', "<caret>", "{<caret>")
        doCharTest('{', "{<caret>", "{{<caret>")
        doCharTest('{', "{{<caret>", "{{{<caret>")
    }

    fun testFormatOnCloseBlockCompleted1() {
        doCharTest('}',

                "{% if foo %}\n" +
                        "    stuff\n" +
                        "    {% endif %<caret>",

                "{% if foo %}\n" +
                        "    stuff\n" +
                        "{% endif %}<caret>")
    }

    fun testFormatOnCloseBlockCompleted2() {
        doCharTest('}',

                "{% if foo %}\n" +
                        "    stuff\n" +
                        "    {% endif %<caret> other stuff",

                "{% if foo %}\n" +
                        "    stuff\n" +
                        "{% endif %}<caret> other stuff")
    }

    fun testFormatOnCloseBlockCompleted3() {
        doCharTest('}',

                "{% if foo %}\n" +
                        "    stuff\n" +
                        "    {% endif %<caret>\n" +
                        "other stuff",

                "{% if foo %}\n" +
                        "    stuff\n" +
                        "{% endif %}<caret>\n" +
                        "other stuff")
    }

    fun testFormatDisabledCloseBlockCompleted() {
        val previousFormatSetting = TwigConfig.isFormattingEnabled
        TwigConfig.isFormattingEnabled = false

        doCharTest('}',

                "{% if foo %}\n" +
                        "    stuff\n" +
                        "    {% endif %<caret>",

                "{% if foo %}\n" +
                        "    stuff\n" +
                        "    {% endif %}<caret>")

        TwigConfig.isFormattingEnabled = previousFormatSetting
    }

    fun testFormatOnSimpleInverseCompleted1() {
        doCharTest('}',

                "{% if foo %}\n" +
                        "    if stuff\n" +
                        "    {% else %<caret>",

                "{% if foo %}\n" +
                        "    if stuff\n" +
                        "{% else %}<caret>")
    }

    fun testFormatOnSimpleInverseCompleted2() {
        doCharTest('}',

                "{% if foo %}\n" +
                        "    if stuff\n" +
                        "    {% else %<caret> other stuff",

                "{% if foo %}\n" +
                        "    if stuff\n" +
                        "{% else %}<caret> other stuff")
    }

    fun testFormatOnSimpleInverseCompleted3() {
        doCharTest('}',

                "{% if foo %}\n" +
                        "    if stuff\n" +
                        "    {% else %<caret>\n" +
                        "other stuff",

                "{% if foo %}\n" +
                        "    if stuff\n" +
                        "{% else %}<caret>\n" +
                        "other stuff")
    }

    fun testFormatDisabledSimpleInverseCompleted() {
        val previousFormatSetting = TwigConfig.isFormattingEnabled
        TwigConfig.isFormattingEnabled = false

        doCharTest('}',

                "{% if foo %}\n" +
                        "    if stuff\n" +
                        "    {% else %<caret>",

                "{% if foo %}\n" +
                        "    if stuff\n" +
                        "    {% else %}<caret>")

        TwigConfig.isFormattingEnabled = previousFormatSetting
    }

    fun testEnterBetweenBlockTags() {
        doEnterTest(

                "{% if foo %}<caret>{% endif %}",

                "{% if foo %}\n" +
                        "    <caret>\n" +
                        "{% endif %}"
        )
    }

    fun testFormatterDisabledEnterBetweenBlockTags() {
        val previousFormatSetting = TwigConfig.isFormattingEnabled
        TwigConfig.isFormattingEnabled = false

        doEnterTest(

                "{% if foo %}<caret>{% endif %}",

                "{% if foo %}\n" +
                        "<caret>\n" +
                        "{% endif %}"
        )

        TwigConfig.isFormattingEnabled = previousFormatSetting
    }

    fun testEnterNotBetweenBlockTags() {
        doEnterTest(

                "{{ foo }}<caret>{{ foo }}",

                "{{ foo }}\n" + "<caret>{{ foo }}"
        )
    }

    fun testFinishingClosingTag() {
        doCharTest(
                '%',
                "<div class=\"entry\">\n" +
                        "    {% if foo %}test{<caret>\n" +
                        "</div>",

                "<div class=\"entry\">\n" +
                        "    {% if foo %}test{% endif %}<caret>\n" +
                        "</div>"
        )

        doCharTest(
                '/',
                "<div class=\"entry\">\n" +
                        "    {% if foo %}test{<caret>\n" +
                        "</div>",

                "<div class=\"entry\">\n" +
                        "    {% if foo %}test{% endif %}<caret>\n" +
                        "</div>"
        )
    }
}
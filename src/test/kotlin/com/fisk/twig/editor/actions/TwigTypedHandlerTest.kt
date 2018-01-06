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

    fun testNestedStatementBraceAutocomplete() {
        TwigConfig.isAutocompleteEndBracesEnabled = false
        doCharTest('%', "{% if foo %}{<caret>{% endif %}", "{% if foo %}{%<caret>{% endif %}")

        TwigConfig.isAutocompleteEndBracesEnabled = true
        doCharTest('%', "{% if foo %}{<caret>{% endif %}", "{% if foo %}{% <caret> %}{% endif %}")
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

    fun testExpressionWhitespaceControlAutocomplete() {
        doCharTest('-', "foo {{<caret> bar }}", "foo {{-<caret> bar -}}")
        doCharTest('-', "foo {{ bar <caret>}}", "foo {{- bar -<caret>}}")
    }

    fun testStatementWhitespaceControlAutocomplete() {
        doCharTest('-', "foo {% <caret>%}", "foo {%- -<caret>%}")
        doCharTest('-', "foo {%<caret> if bar %}", "foo {%-<caret> if bar -%}")
        doCharTest('-', "foo {% if bar <caret>%}", "foo {%- if bar -<caret>%}")
    }

    fun testNoWhitespaceControlAutocompleteIfTokensExist() {
        // Make sure we do not add the whitespace control character if it's already there
        doCharTest('-', "foo {%<caret> if bar -%}", "foo {%-<caret> if bar -%}")
        doCharTest('-', "foo {%- if bar <caret>%}", "foo {%- if bar -<caret>%}")
        doCharTest('-', "foo {{<caret> bar -}}", "foo {{-<caret> bar -}}")
        doCharTest('-', "foo {{- bar <caret>}}", "foo {{- bar -<caret>}}")
        doCharTest('-', "foo {%-<caret> if bar -%}", "foo {%--<caret> if bar -%}")
        doCharTest('-', "foo {%- if bar <caret>-%}", "foo {%- if bar -<caret>-%}")
        doCharTest('-', "foo {{-<caret> bar -}}", "foo {{--<caret> bar -}}")
        doCharTest('-', "foo {{- bar <caret>-}}", "foo {{- bar -<caret>-}}")
    }

    fun testUglyWhitespaceControlAutocomplete() {
        doCharTest('-', "foo {%<caret>if bar%}", "foo {%-<caret>if bar-%}")
    }

    fun testNoWhitespaceControlAutocompleteIfSpaced() {
        // We don't want whitespace complete to work unless the cursor is touching the bracket
        // Otherwise a user may just be typing a regular minus for an expression
        doCharTest('-', "foo {{ bar <caret> }}", "foo {{ bar -<caret> }}")
        doCharTest('-', "foo {{ <caret>bar }}", "foo {{ -<caret>bar }}")
    }

    fun testSimpleExpressions() {
        // ensure that nothing special happens for regular expressions, whether autoGenerateCloseTag is enabled or not

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
        TwigConfig.isAutocompleteEndBracesEnabled = true
        doCharTest('{', "<caret>", "{<caret>")
        doCharTest('{', "{<caret>", "{{ <caret> }}")
        doCharTest('{', "{{<caret>", "{{{ <caret> }}")

        TwigConfig.isAutocompleteEndBracesEnabled = false
        doCharTest('{', "<caret>", "{<caret>")
        doCharTest('{', "{<caret>", "{{<caret>")
        doCharTest('{', "{{<caret>", "{{{<caret>")
    }

    fun testEnterBetweenBlockTags() {
        doEnterTest(

                "{% if foo %}<caret>{% endif %}",

                "{% if foo %}\n" +
                        "    <caret>\n" +
                        "{% endif %}"
        )
    }

    fun testEnterBetweenNestedBlockTags() {
        doEnterTest(

                "{% block foo %}\n" +
                        "    {% if foo %}<caret>{% endif %}\n" +
                        "{% endblock %}",

                "{% block foo %}\n" +
                        "    {% if foo %}\n" +
                        "        <caret>\n" +
                        "    {% endif %}\n" +
                        "{% endblock %}"
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
}
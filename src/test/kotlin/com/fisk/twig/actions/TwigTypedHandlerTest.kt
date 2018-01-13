package com.fisk.twig.actions

import com.fisk.twig.formatting.FormatterTestSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsManager

class TwigTypedHandlerTest : TwigActionHandlerTest() {
    private var formatterTestSettings: FormatterTestSettings? = null

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()

        formatterTestSettings = FormatterTestSettings(CodeStyleSettingsManager.getSettings(project))
        formatterTestSettings!!.setUp()
    }

    @Throws(Exception::class)
    override fun tearDown() {
        formatterTestSettings!!.tearDown()

        super.tearDown()
    }

    /**
     * Test nothing breaks braces when in expression context
     */
    fun testExpressionBraceContext() {
        doCharTest('[', "{<caret>", "{[<caret>")
        doCharTest('[', "{{ <caret> }}", "{{ [<caret>] }}")
        doCharTest('[', "{{<caret>}}", "{{[<caret>]}}")
        doCharTest('[', "{% if <caret> %}", "{% if [<caret>] %}")
        doCharTest('[', "{% <caret> %}", "{% [<caret> %}")

        doCharTest('(', "{<caret>", "{(<caret>")
        doCharTest('(', "{{ <caret> }}", "{{ (<caret>) }}")
        doCharTest('(', "{{<caret>}}", "{{(<caret>)}}")
        doCharTest('(', "{% if <caret> %}", "{% if (<caret>) %}")
        doCharTest('(', "{% <caret> %}", "{% (<caret> %}")

        doCharTest('(', "{% if (<caret>) %}", "{% if ((<caret>)) %}")
        doCharTest('(', "{% if (1 + 2) + <caret> %}", "{% if (1 + 2) + (<caret>) %}")

//        doCharTest('{', "{{ <caret> }}", "{{ {<caret>} }}")
//        doCharTest('{', "{{<caret>}}", "{{{<caret>}}}")
//        doCharTest('{', "{% if <caret> %}", "{% if {<caret>} %}")
//        doCharTest('{', "{% <caret> %}", "{% {<caret> %}")
    }

    fun testStatementBraceAutocomplete() {
        doCharTest('%', "foo {<caret>", "foo {% <caret> %}")
    }

    fun testNestedBraceAutocomplete() {
        doCharTest('%', "{% if foo %}{<caret>{% endif %}", "{% if foo %}{% <caret> %}{% endif %}")
        doCharTest('{', "{% if foo %}{<caret>{% endif %}", "{% if foo %}{{ <caret> }}{% endif %}")
    }

    fun testExpressionBraceAutocomplete() {
        doCharTest('{', "foo {<caret>", "foo {{ <caret> }}")
    }

    fun testCommentBraceAutocomplete() {
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

        doCharTest('}', "{{ foo }<caret>", "{{ foo }}<caret>")
        doCharTest('}', "{{ foo|bar|baz }<caret>", "{{ foo|bar|baz }}<caret>")

        // test when caret is not at file boundary
        doCharTest('}', "{{ foo }<caret>some\nother stuff", "{{ foo }}<caret>some\nother stuff")
        doCharTest('}', "{{ foo|bar|baz }<caret>some\nother stuff", "{{ foo|bar|baz }}<caret>some\nother stuff")
    }

    /**
     * Our typed handler relies on looking a couple of characters back
     * make sure we're well behaved when there are none.
     */
    fun testFirstCharTyped() {
        doCharTest('}', "<caret>", "}<caret>")
    }

    /**
     * Ensure that IDEA does not provide any automatic "}" insertion
     */
    fun testSuppressNativeBracketInsert() {
        doCharTest('{', "<caret>", "{<caret>")
        doCharTest('{', "{<caret>", "{{ <caret> }}")
        doCharTest('{', "{{<caret>", "{{{ <caret> }}")
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

    fun testEnterNotBetweenBlockTags() {
        doEnterTest(

                "{{ foo }}<caret>{{ foo }}",

                "{{ foo }}\n" + "<caret>{{ foo }}"
        )
    }
}
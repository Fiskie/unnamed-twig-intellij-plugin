package com.fisk.twig.format

import com.fisk.twig.TwigLanguage
import com.fisk.twig.config.TwigConfig
import com.fisk.twig.ide.pages.TwigCodeStyleSettings
import com.intellij.lang.html.HTMLLanguage
import com.intellij.openapi.fileTypes.StdFileTypes
import com.intellij.psi.codeStyle.CodeStyleSettingsManager
import com.intellij.psi.codeStyle.CommonCodeStyleSettings

class TwigFormatterIndentTest : TwigFormatterTest() {
    val twigSettings: TwigCodeStyleSettings
        get() = CodeStyleSettingsManager.getSettings(project).getCustomSettings(TwigCodeStyleSettings::class.java)

    val htmlSettings: CommonCodeStyleSettings
        get() = CodeStyleSettingsManager.getSettings(project).getCommonSettings(HTMLLanguage.INSTANCE)

    /**
     * Sanity check that we respect non-default (i.e. 4) indent sizes
     */
    fun testHTMLHonorsNonDefaultIndentSize() {
        val prevIndent = htmlSettings.indentOptions?.INDENT_SIZE
        htmlSettings.indentOptions?.INDENT_SIZE = 2

        doStringBasedTest(
                "{% if foo %}\n" +
                        "    <div>\n" +
                        "{{ bar }}\n" +
                        "    </div>\n" +
                        "{% endif %}",

                "{% if foo %}\n" +
                        "  <div>\n" +
                        "    {{ bar }}\n" +
                        "  </div>\n" +
                        "{% endif %}"
        )

        htmlSettings.indentOptions?.INDENT_SIZE = prevIndent
    }

    fun testTagSpacingFix() {
        doStringBasedTest(
                "{{   foo   }}",
                "{{ foo }}"
        )

        doStringBasedTest(
                "{{foo}}",
                "{{ foo }}"
        )

        doStringBasedTest(
                "{%   if foo   %}",
                "{% if foo %}"
        )

        doStringBasedTest(
                "{%if foo%}",
                "{% if foo %}"
        )

        doStringBasedTest(
                "{%-if foo-%}",
                "{%- if foo -%}"
        )
    }

    fun testDisabledTagSpacing() {
        val prevExprSetting = twigSettings.SPACES_IN_EXPRESSION_TAGS
        twigSettings.SPACES_IN_EXPRESSION_TAGS = false
        doStringBasedTest("{{ foo }}", "{{foo}}")
        doStringBasedTest("{{foo}}", "{{foo}}")
        twigSettings.SPACES_IN_EXPRESSION_TAGS = prevExprSetting


        val prevStatementSetting = twigSettings.SPACES_IN_STATEMENT_TAGS
        twigSettings.SPACES_IN_STATEMENT_TAGS = false
        doStringBasedTest("{% if foo %}", "{%if foo%}")
        doStringBasedTest("{%if foo%}", "{%if foo%}")
        twigSettings.SPACES_IN_STATEMENT_TAGS = prevStatementSetting
    }

    fun testSimpleExpression() {
        doStringBasedTest(
                "{{ foo }}",
                "{{ foo }}"
        )
    }

    fun testSimpleBlock() {
        doStringBasedTest(

                "{% if foo %}\n" +
                        "{{ bar }}\n" +
                        "{% endif %}\n",

                "{% if foo %}\n" +
                        "    {{ bar }}\n" +
                        "{% endif %}\n"
        )
    }

    fun testNestedBlocks() {
        doStringBasedTest(
                "{% if foo %}\n" +
                        "{% if bar %}\n" +
                        "{% if bat %}\n" +
                        "{{ baz }}\n" +
                        "{% endif %}\n" +
                        "{% endif %}\n" +
                        "{% endif %}",
                "{% if foo %}\n" +
                        "    {% if bar %}\n" +
                        "        {% if bat %}\n" +
                        "            {{ baz }}\n" +
                        "        {% endif %}\n" +
                        "    {% endif %}\n" +
                        "{% endif %}"
        )
    }

    fun testSimpleExpressionInDiv() {
        doStringBasedTest(
                "<div>\n" +
                        "{{ foo }}\n" +
                        "</div>",
                "<div>\n" +
                        "    {{ foo }}\n" +
                        "</div>"
        )
    }

    fun testMarkupInBlock() {
        doStringBasedTest(
                "{% if foo %}\n" +
                        "<span></span>\n" +
                        "{% endif %}",
                "{% if foo %}\n" +
                        "    <span></span>\n" +
                        "{% endif %}"
        )
    }

    fun testSimpleBlockInDiv() {
        doStringBasedTest(

                "<div>\n" +
                        "{% if foo %}\n" +
                        "{{ bar }}\n" +
                        "{% endif %}\n" +
                        "</div>",

                "<div>\n" +
                        "    {% if foo %}\n" +
                        "        {{ bar }}\n" +
                        "    {% endif %}\n" +
                        "</div>"
        )
    }

    fun testAttributeExpressions() {
        doStringBasedTest(

                "<div {{ foo }}>\n" +
                        "<div class=\"{{ bar }}\">\n" +
                        "sweeet\n" +
                        "</div>\n" +
                        "</div>",

                "<div {{ foo }}>\n" +
                        "    <div class=\"{{ bar }}\">\n" +
                        "        sweeet\n" +
                        "    </div>\n" +
                        "</div>"
        )
    }

    fun testMixedContentInDiv1() {
        doStringBasedTest(

                ("<div>\n" +
                        "{% if foo %}\n" +
                        "<span class=\"{{ bat }}\">{{ bar }}</span>\n" +
                        "{% endif %}\n" +
                        "</div>"),

                ("<div>\n" +
                        "    {% if foo %}\n" +
                        "        <span class=\"{{ bat }}\">{{ bar }}</span>\n" +
                        "    {% endif %}\n" +
                        "</div>")
        )
    }

    fun testMixedContentInDiv2() {
        doStringBasedTest(

                ("<div>\n" +
                        "{% if foo %}\n" +
                        "bar {{ baz }}\n" +
                        "{% endif %}\n" +
                        "</div>"),

                ("<div>\n" +
                        "    {% if foo %}\n" +
                        "        bar {{ baz }}\n" +
                        "    {% endif %}\n" +
                        "</div>")
        )
    }

    fun testSimpleExpressionInNestedDiv() {
        doStringBasedTest(

                ("{% if foo %}\n" +
                        "    <div>\n" +
                        "{{ bar }}\n" +
                        "    </div>\n" +
                        "{% endif %}"),

                ("{% if foo %}\n" +
                        "    <div>\n" +
                        "        {{ bar }}\n" +
                        "    </div>\n" +
                        "{% endif %}")
        )
    }

    fun testBlockExpressionInNestedDiv() {
        doStringBasedTest(

                ("{% if foo %}\n" +
                        "<div>\n" +
                        "{% if bar %}\n" +
                        "stuff\n" +
                        "{% endif %}\n" +
                        "</div>\n" +
                        "{% endif %}"),

                ("{% if foo %}\n" +
                        "    <div>\n" +
                        "        {% if bar %}\n" +
                        "            stuff\n" +
                        "        {% endif %}\n" +
                        "    </div>\n" +
                        "{% endif %}")
        )
    }

    fun testNestedDivsInBlock() {
        doStringBasedTest(

                ("{% if foo %}\n" +
                        "<div>\n" +
                        "<div>\n" +
                        "<div>\n" +
                        "{{ bar }}\n" +
                        "{% if foo %}\n" +
                        "{% endif %}\n" +
                        "</div>\n" +
                        "</div>\n" +
                        "</div>\n" +
                        "{% endif %}"),

                ("{% if foo %}\n" +
                        "    <div>\n" +
                        "        <div>\n" +
                        "            <div>\n" +
                        "                {{ bar }}\n" +
                        "                {% if foo %}\n" +
                        "                {% endif %}\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "{% endif %}")
        )
    }
}
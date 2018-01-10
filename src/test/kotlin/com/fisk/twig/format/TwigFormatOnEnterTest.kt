package com.fisk.twig.format

import com.fisk.twig.config.TwigConfig
import com.fisk.twig.ide.actions.TwigActionHandlerTest

class TwigFormatOnEnterTest : TwigActionHandlerTest(), TwigFormattingModelBuilderTest {
    fun testSimpleExpression() {
        doEnterTest(

                "{{ foo }}<caret>",

                "{{ foo }}\n" + "<caret>"
        )
    }

    fun testSimpleBlock1() {
        doEnterTest(

                "{% if foo %}<caret>",

                "{% if foo %}\n" + "    <caret>"
        )
    }

    fun testSimpleBlock2() {
        doEnterTest(

                "{% if foo %}\n" + "    {{ bar }}<caret>htmlPadding",

                "{% if foo %}\n" +
                        "    {{ bar }}\n" +
                        "    <caret>htmlPadding"
        )
    }

    fun testSimpleBlock3() {
        doEnterTest(
                "{% if foo %}\n" +
                        "    {{ bar }}<caret>\n" +
                        "{% endif %}\n",

                "{% if foo %}\n" +
                        "    {{ bar }}\n" +
                        "    <caret>\n" +
                        "{% endif %}\n")
    }

    fun testNestedBlocks1() {
        doEnterTest(

                "{% if foo %}\n" +
                        "{% if bar %}\n" +
                        "{% if bat %}<caret>\n" +
                        "{{ baz }}\n" +
                        "{% endif %}\n" +
                        "{% endif %}\n" +
                        "{% endif %}",

                "{% if foo %}\n" +
                        "{% if bar %}\n" +
                        "{% if bat %}\n" +
                        "    <caret>\n" +
                        "{{ baz }}\n" +
                        "{% endif %}\n" +
                        "{% endif %}\n" +
                        "{% endif %}"
        )
    }

    fun testNestedBlocks2() {
        doEnterTest(

                "{% if foo %}\n" +
                        "    {% if bar %}\n" +
                        "        {% if bat %}<caret>\n" +
                        "            {{ baz }}\n" +
                        "        {% endif %}\n" +
                        "    {% endif %}\n" +
                        "{% endif %}",

                "{% if foo %}\n" +
                        "    {% if bar %}\n" +
                        "        {% if bat %}\n" +
                        "            <caret>\n" +
                        "            {{ baz }}\n" +
                        "        {% endif %}\n" +
                        "    {% endif %}\n" +
                        "{% endif %}"
        )
    }

    fun testNestedBlocks3() {
        doEnterTest(

                "{% if foo %}\n" +
                        "    {% if bar %}\n" +
                        "        {% if bat %}\n" +
                        "            {{ baz }}<caret>\n" +
                        "        {% endif %}\n" +
                        "    {% endif %}\n" +
                        "{% endif %}",

                "{% if foo %}\n" +
                        "    {% if bar %}\n" +
                        "        {% if bat %}\n" +
                        "            {{ baz }}\n" +
                        "            <caret>\n" +
                        "        {% endif %}\n" +
                        "    {% endif %}\n" +
                        "{% endif %}"
        )
    }

    fun testSimpleExpressionInDiv1() {
        doEnterTest(

                "<div><caret>\n" +
                        "    {{foo}}\n" +
                        "</div>",

                "<div>\n" +
                        "    <caret>\n" +
                        "    {{foo}}\n" +
                        "</div>"
        )
    }

    fun testSimpleExpressionInDiv2() {
        doEnterTest(

                "<div>\n" +
                        "    {{ foo }}<caret>\n" +
                        "</div>",

                "<div>\n" +
                        "    {{ foo }}\n" +
                        "    <caret>\n" +
                        "</div>"
        )
    }

    fun testMarkupInBlockStache1() {
        doEnterTest(

                "{% if foo %}\n" +
                        "    <span></span><caret>\n" +
                        "{% endif %}",

                "{% if foo %}\n" +
                        "    <span></span>\n" +
                        "    <caret>\n" +
                        "{% endif %}"
        )
    }

    fun testMarkupInBlockStache2() {
        doEnterTest(

                "{% if foo %}<caret>\n" +
                        "    <span></span>\n" +
                        "{% endif %}",

                "{% if foo %}\n" +
                        "    <caret>\n" +
                        "    <span></span>\n" +
                        "{% endif %}"
        )
    }

    fun testMarkupInBlockStache3() {
        doEnterTest(

                "{% if foo %}\n" + "    <span></span><caret>",

                "{% if foo %}\n" +
                        "    <span></span>\n" +
                        "    <caret>"
        )
    }

    fun testEmptyBlockInDiv1() {
        doEnterTest(

                "<div>\n" +
                        "    {% if foo %}<caret>\n" +
                        "    {% endif %}\n" +
                        "</div>",

                "<div>\n" +
                        "    {% if foo %}\n" +
                        "        <caret>\n" +
                        "    {% endif %}\n" +
                        "</div>"
        )
    }

    fun testEmptyBlockInDiv2() {
        doEnterTest(

                "<div>\n" +
                        "    {% if foo %}<caret>{% endif %}\n" +
                        "</div>",

                "<div>\n" +
                        "    {% if foo %}\n" +
                        "        <caret>\n" +
                        "    {% endif %}\n" +
                        "</div>"
        )
    }

    fun testEmptyBlockInDiv3() {
        doEnterTest(

                "<div>\n" +
                        "    {% if foo %}<caret>\n" +
                        "htmlPadding",

                "<div>\n" +
                        "    {% if foo %}\n" +
                        "        <caret>\n" +
                        "htmlPadding"
        )
    }

    fun testEmptyBlockInDiv4() {
        doEnterTest(

                "<div>\n" + "{% if foo %}<caret>{% endif %}",

                "<div>\n" +
                        "{% if foo %}\n" +
                        "    <caret>\n" +
                        "{% endif %}"
        )
    }

    fun testSimpleBlockInDiv1() {
        doEnterTest(

                "<div>\n" +
                        "{% if foo %}\n" +
                        "{{ bar }}<caret>\n" +
                        "{% endif %}\n" +
                        "</div>",

                "<div>\n" +
                        "{% if foo %}\n" +
                        "{{ bar }}\n" +
                        "    <caret>\n" +
                        "{% endif %}\n" +
                        "</div>"
        )
    }

    fun testSimpleBlockInDiv2() {
        doEnterTest(

                "<div>\n" +
                        "    {% if foo %}\n" +
                        "        {{ bar }}<caret>\n" +
                        "    {% endif %}\n" +
                        "</div>",

                "<div>\n" +
                        "    {% if foo %}\n" +
                        "        {{ bar }}\n" +
                        "        <caret>\n" +
                        "    {% endif %}\n" +
                        "</div>"
        )
    }

    fun testSimpleBlockInDiv3() {
        doEnterTest(

                "<div>\n" +
                        "    {% if foo %}\n" +
                        "        {{ bar }}\n" +
                        "    {% endif %}<caret>\n" +
                        "</div>",

                "<div>\n" +
                        "    {% if foo %}\n" +
                        "        {{ bar }}\n" +
                        "    {% endif %}\n" +
                        "    <caret>\n" +
                        "</div>"
        )
    }

    fun testSimpleBlockInDiv7() {
        doEnterTest(

                "<div>\n" +
                        "    {% if foo %}<caret>\n" +
                        "        {{ bar }}\n" +
                        "    {% endif %}",

                "<div>\n" +
                        "    {% if foo %}\n" +
                        "    <caret>\n" + // NOTE: this is not ideal, but it's tough to get the formatting right when there's unclosed html elements

                        "        {{ bar }}\n" +
                        "    {% endif %}"
        )
    }

    fun testSimpleBlockInDiv8() {
        doEnterTest(

                "<div>\n" +
                        "    {% if foo %}<caret>\n" +
                        "        {{ bar }}\n" +
                        "    {% endif %}\n" +
                        "</div>",

                "<div>\n" +
                        "    {% if foo %}\n" +
                        "        <caret>\n" +
                        "        {{ bar }}\n" +
                        "    {% endif %}\n" +
                        "</div>"
        )
    }

    fun testAttributeExpressions1() {
        doEnterTest(

                "<div {{foo}}><caret>\n" +
                        "    <div class=\"{{ bar }}\">\n" +
                        "        sweeet\n" +
                        "    </div>\n" +
                        "</div>",

                "<div {{foo}}>\n" +
                        "    <caret>\n" +
                        "    <div class=\"{{ bar }}\">\n" +
                        "        sweeet\n" +
                        "    </div>\n" +
                        "</div>"
        )
    }

    fun testAttributeExpressions2() {
        doEnterTest(

                "<div {{foo}}>\n" +
                        "    <div class=\"{{ bar }}\"><caret>\n" +
                        "        sweeet\n" +
                        "    </div>\n" +
                        "</div>",

                "<div {{foo}}>\n" +
                        "    <div class=\"{{ bar }}\">\n" +
                        "        <caret>\n" +
                        "        sweeet\n" +
                        "    </div>\n" +
                        "</div>"
        )
    }

    fun testAttributeExpressions3() {
        doEnterTest(

                "<div {{foo}}>\n" +
                        "    <div class=\"{{ bar }}\">\n" +
                        "        sweeet<caret>\n" +
                        "    </div>\n" +
                        "</div>",

                "<div {{foo}}>\n" +
                        "    <div class=\"{{ bar }}\">\n" +
                        "        sweeet\n" +
                        "        <caret>\n" +
                        "    </div>\n" +
                        "</div>"
        )
    }

    fun testAttributeExpressions4() {
        doEnterTest(

                "<div {{foo}}><caret>",

                "<div {{foo}}>\n" + "    <caret>"
        )
    }

    fun testAttributeExpressions5() {
        doEnterTest(

                "<div {{foo}}>\n" + "    <div class=\"{{ bar }}\"><caret>",

                "<div {{foo}}>\n" +
                        "    <div class=\"{{ bar }}\">\n" +
                        "        <caret>"
        )
    }

    fun testAttributeExpressions6() {
        doEnterTest(

                "<div {{foo}}>\n" +
                        "    <div class=\"{{ bar }}\">\n" +
                        "        sweeet<caret>",

                "<div {{foo}}>\n" +
                        "    <div class=\"{{ bar }}\">\n" +
                        "        sweeet\n" +
                        "        <caret>"
        )
    }

    fun testMixedContentInDiv1() {
        doEnterTest(

                "<div>\n" +
                        "    {% if foo %}\n" +
                        "        <span class=\"{{ bat }}\">{{ bar }}</span><caret>\n" +
                        "    {% endif %}\n" +
                        "</div>",

                "<div>\n" +
                        "    {% if foo %}\n" +
                        "        <span class=\"{{ bat }}\">{{ bar }}</span>\n" +
                        "        <caret>\n" +
                        "    {% endif %}\n" +
                        "</div>"
        )
    }

    fun testMixedContentInDiv2() {
        doEnterTest(

                "<div>\n" +
                        "    {% if foo %}<caret>\n" +
                        "        <span class=\"{{ bat }}\">{{ bar }}</span>\n" +
                        "    {% endif %}\n" +
                        "</div>",

                "<div>\n" +
                        "    {% if foo %}\n" +
                        "        <caret>\n" +
                        "        <span class=\"{{ bat }}\">{{ bar }}</span>\n" +
                        "    {% endif %}\n" +
                        "</div>"
        )
    }

    fun testMixedContentInDiv3() {
        doEnterTest(

                "<div>\n" +
                        "    {% if foo %}\n" +
                        "        <span class=\"{{ bat }}\">{{ bar }}</span><caret>\n" +
                        "    {% endif %}\n" +
                        "</div>",

                "<div>\n" +
                        "    {% if foo %}\n" +
                        "        <span class=\"{{ bat }}\">{{ bar }}</span>\n" +
                        "        <caret>\n" +
                        "    {% endif %}\n" +
                        "</div>"
        )
    }

    fun testMixedContentInDiv4() {
        doEnterTest(

                "<div>\n" +
                        "    {% if foo %}\n" +
                        "        <span class=\"{{ bat }}\">{{ bar }}</span>\n" +
                        "    {% endif %}<caret>\n" +
                        "</div>",

                "<div>\n" +
                        "    {% if foo %}\n" +
                        "        <span class=\"{{ bat }}\">{{ bar }}</span>\n" +
                        "    {% endif %}\n" +
                        "    <caret>\n" +
                        "</div>"
        )
    }

    fun testMixedContentInDiv5() {
        doEnterTest(

                "<div><caret>\n" +
                        "    {% if foo %}\n" +
                        "        <span class=\"{{ bat }}\">{{ bar }}</span>",

                "<div>\n" +
                        "    <caret>\n" +
                        "    {% if foo %}\n" +
                        "        <span class=\"{{ bat }}\">{{ bar }}</span>"
        )
    }

    fun testMixedContentInDiv6() {
        doEnterTest(

                "<div>\n" +
                        "    {% if foo %}<caret>\n" +
                        "        <span class=\"{{ bat }}\">{{ bar }}</span>",

                "<div>\n" +
                        "    {% if foo %}\n" +
                        "        <caret>\n" +
                        "        <span class=\"{{ bat }}\">{{ bar }}</span>"
        )
    }

    fun testEmptyLinesAfterOpenBlock1() {
        doEnterTest(

                "{% if foo %}\n" +
                        "    \n" +
                        "    \n" +
                        "    \n" +
                        "    <caret>\n" +
                        "    \n",

                "{% if foo %}\n" +
                        "    \n" +
                        "    \n" +
                        "    \n" +
                        "    \n" +
                        "    <caret>\n" +
                        "    \n"
        )
    }

    fun testEmptyLinesAfterOpenBlock2() {
        doEnterTest(

                "{% if foo %}\n" +
                        "    \n" +
                        "    \n" +
                        "{% else %}\n" +
                        "    \n" +
                        "    \n" +
                        "    <caret>\n" +
                        "    \n" +
                        "    \n",

                "{% if foo %}\n" +
                        "    \n" +
                        "    \n" +
                        "{% else %}\n" +
                        "    \n" +
                        "    \n" +
                        "    \n" +
                        "    <caret>\n" +
                        "    \n" +
                        "    \n"
        )
    }

    fun testSimpleExpressionInNestedDiv1() {
        doEnterTest(

                "{% if foo %}\n" +
                        "    <div><caret>\n" +
                        "        {{ bar }}\n" +
                        "    </div>\n" +
                        "{% endif %}",

                "{% if foo %}\n" +
                        "    <div>\n" +
                        "        <caret>\n" +
                        "        {{ bar }}\n" +
                        "    </div>\n" +
                        "{% endif %}"
        )
    }

    fun testSimpleExpressionInNestedDiv2() {
        doEnterTest(

                "{% if foo %}\n" +
                        "    <div>\n" +
                        "        {{ bar }}<caret>\n" +
                        "    </div>\n" +
                        "{% endif %}",

                "{% if foo %}\n" +
                        "    <div>\n" +
                        "        {{ bar }}\n" +
                        "        <caret>\n" +
                        "    </div>\n" +
                        "{% endif %}"
        )
    }

    fun testBlockStatementInNestedDiv1() {
        doEnterTest(

                "{% if foo %}\n" +
                        "    <div><caret>\n" +
                        "        {% if bar %}\n" +
                        "            stuff\n" +
                        "        {% endif %}\n" +
                        "    </div>\n" +
                        "{% endif %}",

                "{% if foo %}\n" +
                        "    <div>\n" +
                        "        <caret>\n" +
                        "        {% if bar %}\n" +
                        "            stuff\n" +
                        "        {% endif %}\n" +
                        "    </div>\n" +
                        "{% endif %}"
        )
    }

    fun testBlockStatementInNestedDiv2() {
        doEnterTest(

                "{% if foo %}\n" +
                        "    <div>\n" +
                        "        {% if bar %}<caret>\n" +
                        "            stuff\n" +
                        "        {% endif %}\n" +
                        "    </div>\n" +
                        "{% endif %}",

                "{% if foo %}\n" +
                        "    <div>\n" +
                        "        {% if bar %}\n" +
                        "            <caret>\n" +
                        "            stuff\n" +
                        "        {% endif %}\n" +
                        "    </div>\n" +
                        "{% endif %}"
        )
    }

    fun testBlockStatementInNestedDiv3() {
        doEnterTest(

                "{% if foo %}\n" +
                        "    <div>\n" +
                        "        {% if bar %}\n" +
                        "            stuff<caret>\n" +
                        "        {% endif %}\n" +
                        "    </div>\n" +
                        "{% endif %}",

                "{% if foo %}\n" +
                        "    <div>\n" +
                        "        {% if bar %}\n" +
                        "            stuff\n" +
                        "            <caret>\n" +
                        "        {% endif %}\n" +
                        "    </div>\n" +
                        "{% endif %}"
        )
    }

    fun testBlockStatementInNestedDiv4() {
        doEnterTest(

                "{% if foo %}\n" +
                        "    <div>\n" +
                        "        {% if bar %}\n" +
                        "            stuff\n" +
                        "        {% endif %}<caret>\n" +
                        "    </div>\n" +
                        "{% endif %}",

                "{% if foo %}\n" +
                        "    <div>\n" +
                        "        {% if bar %}\n" +
                        "            stuff\n" +
                        "        {% endif %}\n" +
                        "        <caret>\n" +
                        "    </div>\n" +
                        "{% endif %}"
        )
    }
}

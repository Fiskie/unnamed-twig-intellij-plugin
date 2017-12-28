package com.fisk.twig.pages

import com.fisk.twig.TwigBundle
import com.fisk.twig.TwigHighlighter
import com.fisk.twig.config.TwigIcons
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage

class TwigColorsPage : ColorSettingsPage {

    override fun getDisplayName(): String {
        return TwigBundle.message("twig.files.file.type.description")
    }

    override fun getIcon() = TwigIcons.file_icon

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> {
        return ATTRS
    }

    override fun getColorDescriptors(): Array<ColorDescriptor> {
        return ColorDescriptor.EMPTY_ARRAY
    }

    override fun getHighlighter(): SyntaxHighlighter {
        return TwigHighlighter()
    }

    override fun getDemoText(): String {
        return "{% include 'root.html' with {foo: bar, arr: [1, 2, 3]} %} \n" +
                "{# this is a comment #}\n" +
                "{% block content %}\n" +
                "    {% for key, value in list if key is in [\"foo\", \"bar\"] %}\n" +
                "        {% if value.bool == true and value.data is odd %}\n" +
                "            <p class=\"intro-text\">\n" +
                "                {{ value['name']|capitalize }}\n" +
                "                {{ function(value.data) * 1234 }}\n" +
                "            </p>\n" +
                "        {% endif %}\n" +
                "    {% endfor %}\n" +
                "{% endblock %}\n"
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? {
        return null
    }

    companion object {
        private val ATTRS: Array<AttributesDescriptor>

        init {
            val attrs = arrayOfNulls<AttributesDescriptor>(TwigHighlighter.DISPLAY_NAMES.size)
            val textAttributesKeys = TwigHighlighter.DISPLAY_NAMES.keys
            val keys = textAttributesKeys.toTypedArray()
            for (i in keys.indices) {
                val key = keys[i]
                val name = TwigHighlighter.DISPLAY_NAMES[key]?.getFirst()

                name?.let {
                    attrs[i] = AttributesDescriptor(name, key)
                }
            }

            ATTRS = attrs as Array<AttributesDescriptor>
        }
    }
}
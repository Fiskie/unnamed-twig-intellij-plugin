package com.fisk.twig.formatting.settings

import com.fisk.twig.TwigLanguage
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider.SettingsType.*

class TwigLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {
    override fun getLanguage() = TwigLanguage.INSTANCE

    override fun getCodeSample(settingsType: LanguageCodeStyleSettingsProvider.SettingsType) = CODE_SAMPLE

    override fun getIndentOptionsEditor() = SmartIndentOptionsEditor()

    override fun getDefaultCommonSettings(): CommonCodeStyleSettings? {
        return CommonCodeStyleSettings(language).apply {
            RIGHT_MARGIN = 120
            SPACE_AROUND_LOGICAL_OPERATORS = true
            SPACE_AFTER_COMMA = true
            initIndentOptions()
        }
    }

    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
        when (settingsType) {
            SPACING_SETTINGS -> {
                consumer.showStandardOptions(
                        "SPACE_AROUND_LOGICAL_OPERATORS",
                        "SPACE_AFTER_COMMA"
                )

                consumer.showCustomOption(TwigCodeStyleSettings::class.java,
                        "SPACES_IN_EXPRESSION_TAGS",
                        "In expression tags ({{ }})",
                        CodeStyleSettingsCustomizable.SPACES_OTHER)

                consumer.showCustomOption(TwigCodeStyleSettings::class.java,
                        "SPACES_IN_STATEMENT_TAGS",
                        "In statement tags ({% %})",
                        CodeStyleSettingsCustomizable.SPACES_OTHER)
            }

            BLANK_LINES_SETTINGS -> {
                consumer.showStandardOptions(
                        "KEEP_LINE_BREAKS",
                        "KEEP_BLANK_LINES_IN_DECLARATIONS",
                        "KEEP_BLANK_LINES_IN_CODE")

                consumer.showCustomOption(TwigCodeStyleSettings::class.java,
                        "MIN_NUMBER_OF_BLANKS_BETWEEN_ITEMS",
                        "Between declarations:",
                        CodeStyleSettingsCustomizable.BLANK_LINES)
            }

            WRAPPING_AND_BRACES_SETTINGS -> {
                consumer.showStandardOptions(
                        "RIGHT_MARGIN",
                        "WRAP_ON_TYPING")
            }

            else -> {
            }
        }
    }

    companion object {
        val CODE_SAMPLE = "{% embed 'parent.twig' %}\n" +
                "    {% include 'include.twig' with {foo: 'bar'} %}\n" +
                "    {% block header %}\n" +
                "         <div>\n" +
                "             <h4>Items</h4>\n" +
                "             {% for item in items %}\n" +
                "                 <ul>\n" +
                "                     <li>Item: {{ item|foo(1, \"str\") }}</li>\n" +
                "                 </ul>\n" +
                "             {% endfor %}\n" +
                "         </div>\n" +
                "    {% endblock %}\n" +
                "{% endembed %}"
    }
}
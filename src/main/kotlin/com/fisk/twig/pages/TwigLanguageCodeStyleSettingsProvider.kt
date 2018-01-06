package com.fisk.twig.pages

import com.fisk.twig.TwigLanguage
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider

class TwigLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {
    override fun getLanguage() = TwigLanguage.INSTANCE

    override fun getCodeSample(settingsType: LanguageCodeStyleSettingsProvider.SettingsType) = CODE_SAMPLE

    override fun getIndentOptionsEditor() = SmartIndentOptionsEditor()

    override fun getDefaultCommonSettings(): CommonCodeStyleSettings? {
        val defaultSettings = CommonCodeStyleSettings(language)
        val indentOptions = defaultSettings.initIndentOptions()
        indentOptions.INDENT_SIZE = 4
        indentOptions.CONTINUATION_INDENT_SIZE = 8
        indentOptions.TAB_SIZE = 4
        defaultSettings.RIGHT_MARGIN = 80
        return defaultSettings
    }

    companion object {
        val CODE_SAMPLE = "{% embed 'parent.twig' %}\n" +
                "    {% include 'include.twig' with {foo: 'bar'} %}\n" +
                "    {% block header %}\n" +
                "         <div>\n" +
                "             Sample\n" +
                "         </div>\n" +
                "    {% endblock %}\n" +
                "{% endembed %}"
    }
}
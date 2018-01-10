package com.fisk.twig.ide.pages

import com.fisk.twig.TwigLanguage
import com.intellij.application.options.SmartIndentOptionsEditor
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider

class TwigLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {
    override fun getLanguage() = TwigLanguage.INSTANCE

    override fun getCodeSample(settingsType: LanguageCodeStyleSettingsProvider.SettingsType) = CODE_SAMPLE

    override fun getIndentOptionsEditor() = SmartIndentOptionsEditor()

    override fun getDefaultCommonSettings(): CommonCodeStyleSettings? {
        return CommonCodeStyleSettings(language).apply {
            val indentOptions = initIndentOptions()

            indentOptions.apply {
                INDENT_SIZE = 4
                CONTINUATION_INDENT_SIZE = 8
                TAB_SIZE = 4
            }

            RIGHT_MARGIN = 120
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
                "                     <li>Items</li>\n" +
                "                 </ul>\n" +
                "             {% endfor %}\n" +
                "         </div>\n" +
                "    {% endblock %}\n" +
                "{% endembed %}"
    }
}
package com.fisk.twig.ide.pages

import com.fisk.twig.TwigLanguage
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.psi.codeStyle.CodeStyleSettings

class TwigCodeStylePanel(
        currentSettings: CodeStyleSettings?,
        settings: CodeStyleSettings?
) : TabbedLanguageCodeStylePanel(TwigLanguage.INSTANCE, currentSettings, settings) {
    override fun initTabs(settings: CodeStyleSettings?) {
        addSpacesTab(settings)
        addBlankLinesTab(settings)
        addWrappingAndBracesTab(settings)
    }
}
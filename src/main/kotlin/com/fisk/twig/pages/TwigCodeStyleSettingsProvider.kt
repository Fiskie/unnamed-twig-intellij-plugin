package com.fisk.twig.pages

import com.fisk.twig.TwigLanguage
import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.openapi.options.Configurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider

class TwigCodeStyleSettingsProvider : CodeStyleSettingsProvider() {
    override fun createSettingsPage(settings: CodeStyleSettings, originalSettings: CodeStyleSettings?): Configurable {
        return object : CodeStyleAbstractConfigurable(settings, originalSettings, "Twig") {
            override fun createPanel(settings: CodeStyleSettings?): CodeStyleAbstractPanel {
                return TwigCodeStylePanel(currentSettings, settings)
            }

            override fun getHelpTopic() = "reference needed" // todo
        }
    }

    override fun getLanguage() = TwigLanguage.INSTANCE

    override fun getConfigurableDisplayName() = "Twig"
}
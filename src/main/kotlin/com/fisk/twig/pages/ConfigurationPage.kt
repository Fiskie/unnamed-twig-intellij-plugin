package com.fisk.twig.pages

import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import javax.swing.JComponent
import javax.swing.JPanel

class ConfigurationPage(val project: Project) : SearchableConfigurable {
    lateinit var basePanel: JPanel

    override fun isModified(): Boolean {
        return false
    }

    override fun disposeUIResources() {

    }

    override fun getId(): String {
        return "editor.preferences.twigImproved"
    }

    override fun getDisplayName(): String {
        return "Twig Improved"
    }

    override fun apply() {

    }

    override fun enableSearch(option: String?): Runnable? {
        return null
    }

    override fun createComponent(): JComponent? {
        return basePanel
    }

    override fun reset() {

    }

    override fun getHelpTopic(): String? {
        return "Big benis :-DD"
    }
}

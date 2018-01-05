package com.fisk.twig.pages

import com.fisk.twig.TwigBundle
import com.fisk.twig.ide.EditingModel
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import javax.swing.DefaultComboBoxModel
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JPanel

class ConfigurationPage(val project: Project) : SearchableConfigurable {
    @JvmField
    var editMode: JComboBox<EditingModel>? = null
    @JvmField
    var basePanel: JPanel? = null

    override fun isModified(): Boolean {
        return false
    }

    override fun disposeUIResources() {

    }

    override fun getId(): String {
        return "editor.preferences.twigImproved"
    }

    override fun getDisplayName(): String {
        return "Twig"
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
        populateEditingModels()
    }

    private fun populateEditingModels() {
        val model = editMode?.model as DefaultComboBoxModel
        val new = EditingModel("new", TwigBundle.message("twig.page.options.editing.model.new"))

        model.addElement(EditingModel("none", TwigBundle.message("twig.page.options.editing.model.none")))
        model.addElement(EditingModel("legacy", TwigBundle.message("twig.page.options.editing.model.legacy")))
        model.addElement(new)

        model.selectedItem = new
    }

    override fun getHelpTopic(): String? {
        return "Twig"
    }
}

package com.fisk.twig.ide.pages

import com.fisk.twig.TwigBundle
import com.fisk.twig.config.TwigConfig
import com.fisk.twig.ide.EditingModel
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.ui.ListCellRendererWrapper
import javax.swing.*

class ConfigurationPage(val project: Project) : SearchableConfigurable {
    @JvmField
    var editModel: JComboBox<EditingModel>? = null
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
        TwigConfig.braceEditingModel = editModel?.selectedItem as String?
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
        val model = editModel?.model as DefaultComboBoxModel<EditingModel>
        val new = EditingModel(EditingModel.NEW, TwigBundle.message("twig.page.options.editing.model.new"))

        model.addElement(EditingModel(EditingModel.NONE, TwigBundle.message("twig.page.options.editing.model.none")))
        model.addElement(EditingModel(EditingModel.LEGACY, TwigBundle.message("twig.page.options.editing.model.legacy")))
        model.addElement(new)

        editModel?.renderer = object : ListCellRendererWrapper<EditingModel>() {
            override fun customize(list: JList<*>?, value: EditingModel?, index: Int, selected: Boolean, hasFocus: Boolean) {
                setText(value?.name)
            }
        }

        model.selectedItem = new
    }

    override fun getHelpTopic(): String? {
        return "Twig"
    }
}

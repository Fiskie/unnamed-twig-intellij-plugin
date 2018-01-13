package com.fisk.twig.config

import com.fisk.twig.config.Property.*
import com.fisk.twig.config.Property.Companion.DISABLED
import com.fisk.twig.config.Property.Companion.ENABLED
import com.intellij.ide.util.PropertiesComponent
import com.intellij.lang.Language
import com.intellij.lang.html.HTMLLanguage
import com.intellij.openapi.project.Project

object TwigConfig {
    val isAutoCollapseBlocksEnabled: Boolean
        get() = getBooleanPropertyValue(AUTO_COLLAPSE_BLOCKS)

    fun setAutoCollapseBlocks(enabled: Boolean) {
        setBooleanPropertyValue(AUTO_COLLAPSE_BLOCKS, enabled)
    }

    private fun getStringPropertyValue(property: Property, project: Project? = null): String {
        return PropertyAccessor(getProperties(project)).getPropertyValue(property)
    }

    private fun getProperties(project: Project?): PropertiesComponent {
        project?.let {
            return PropertiesComponent.getInstance(project)
        }

        return PropertiesComponent.getInstance()
    }


    private fun setStringPropertyValue(property: Property, value: String?, project: Project? = null) {
        PropertyAccessor(getProperties(project)).setPropertyValue(property, value)
    }

    private fun getBooleanPropertyValue(property: Property): Boolean {
        return ENABLED == getStringPropertyValue(property)
    }

    private fun setBooleanPropertyValue(property: Property, enabled: Boolean, project: Project? = null) {
        setStringPropertyValue(property, if (enabled) ENABLED else DISABLED, project)
    }
}

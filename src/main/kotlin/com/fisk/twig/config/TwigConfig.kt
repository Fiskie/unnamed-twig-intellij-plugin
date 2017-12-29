package com.fisk.twig.config

import com.fisk.twig.config.Property.*
import com.fisk.twig.config.Property.Companion.DISABLED
import com.fisk.twig.config.Property.Companion.ENABLED
import com.intellij.ide.util.PropertiesComponent
import com.intellij.lang.Language
import com.intellij.lang.html.HTMLLanguage
import com.intellij.openapi.project.Project

object TwigConfig {
    var isAutoGenerateCloseTagEnabled: Boolean
        get() = getBooleanPropertyValue(AUTO_GENERATE_CLOSE_TAG)
        set(enabled) = setBooleanPropertyValue(AUTO_GENERATE_CLOSE_TAG, enabled)

    var isAutocompleteMustachesEnabled: Boolean
        get() = getBooleanPropertyValue(AUTOCOMPLETE_MUSTACHES)
        set(enabled) = setBooleanPropertyValue(AUTOCOMPLETE_MUSTACHES, enabled)

    var isFormattingEnabled: Boolean
        get() = getBooleanPropertyValue(FORMATTER)
        set(enabled) = setBooleanPropertyValue(FORMATTER, enabled)

    val isAutoCollapseBlocksEnabled: Boolean
        get() = getBooleanPropertyValue(AUTO_COLLAPSE_BLOCKS)

    fun setAutoCollapseBlocks(enabled: Boolean) {
        setBooleanPropertyValue(AUTO_COLLAPSE_BLOCKS, enabled)
    }

    var commenterLanguage: Language?
        get() {
            val id = Language.findLanguageByID(getStringPropertyValue(COMMENTER_LANGUAGE_ID))
            return id ?: HTMLLanguage.INSTANCE
        }
        set(language) = if (language == null) {
            setStringPropertyValue(COMMENTER_LANGUAGE_ID, null)
        } else {
            setStringPropertyValue(COMMENTER_LANGUAGE_ID, language.id)
        }

    fun getRawOpenHtmlAsTwigValue(project: Project): String {
        return getStringPropertyValue(SHOULD_OPEN_HTML, project)
    }

    fun shouldOpenHtmlAsTwig(project: Project): Boolean {
        val value = getRawOpenHtmlAsTwigValue(project)
        return ENABLED == value
    }

    fun setShouldOpenHtmlAsTwig(value: Boolean, project: Project): Boolean {
        setBooleanPropertyValue(SHOULD_OPEN_HTML, value, project)
        return true
    }

    private fun getStringPropertyValue(property: Property, project: Project? = null): String {
        return PropertyAccessor(getProperties(project)).getPropertyValue(property)
    }

    private fun getProperties(project: Project?): PropertiesComponent {
        return if (project == null) PropertiesComponent.getInstance() else PropertiesComponent.getInstance(project)
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
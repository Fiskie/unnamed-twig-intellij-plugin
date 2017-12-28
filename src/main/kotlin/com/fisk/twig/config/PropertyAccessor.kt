package com.fisk.twig.config

import com.intellij.ide.util.PropertiesComponent

/**
 * Class responsible for reads and writes of properties
 */
internal class PropertyAccessor(private val myPropertiesComponent: PropertiesComponent) {

    fun getPropertyValue(property: Property): String {

        // We getOrInit to ensure that the default is written for this user the first time it is fetched
        // This will ensure that users preferences stay stable in the future, even if defaults change
        return myPropertiesComponent.getOrInit(property.stringName, property.default)
    }

    fun setPropertyValue(property: Property, propertyValue: String?) {
        myPropertiesComponent.setValue(property.stringName,
                propertyValue)
    }
}

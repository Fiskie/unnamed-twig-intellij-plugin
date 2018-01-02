package com.fisk.twig.config

import com.intellij.ide.util.PropertiesComponentImpl
import junit.framework.Assert
import org.junit.Test

class PropertyAccessorTest {

    // grab a Property to use in this test.  NOTE: the specific property is not significant.
    private val myTestProperty = Property.FORMATTER

    @Test
    fun testGetPropertyValue() {
        val propertiesComponent = PropertiesComponentImpl.create()
        val originalValue = Property.DISABLED

        // simulate an existing value by setting it directly on the propertiesComponent
        propertiesComponent.setValue(myTestProperty.stringName, originalValue)

        val propertyValue = PropertyAccessor(propertiesComponent).getPropertyValue(myTestProperty)

        Assert.assertEquals("Problem fetching existing property", originalValue, propertyValue)
    }

    @Test
    fun testGetPropertyValueDefaulting() {
        val propertiesComponent = PropertiesComponentImpl.create()

        val expectedValue = myTestProperty.default
        val propertyValue = PropertyAccessor(propertiesComponent).getPropertyValue(myTestProperty)

        Assert.assertEquals("Default value should have been returned", expectedValue, propertyValue)
    }

    @Test
    fun testSetPropertyValue() {
        val propertiesComponent = PropertiesComponentImpl.create()

        val testValue = Property.DISABLED
        PropertyAccessor(propertiesComponent).setPropertyValue(myTestProperty, Property.DISABLED)

        // fetch the value directly to ensure PropertyAccessor didn't mess it up
        val propertiesComponentValue = propertiesComponent.getValue(myTestProperty.stringName)

        Assert.assertEquals("Value was not properly persisted", testValue, propertiesComponentValue)
    }
}

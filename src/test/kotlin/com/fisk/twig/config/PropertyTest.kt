package com.fisk.twig.config

import org.junit.Assert
import org.junit.Test
import java.util.*

class PropertyTest {

    /**
     * This test will fail if properties are added/removed in [com.fisk.twig.config.Property]
     *
     *
     * When/if it fails:
     * - ensure the change is backwards compatible (i.e. when users upgrade, their properties will still be in the same state)
     * - update this test with the new number of properties to get it passing
     */
    @Test
    fun testPropertiesChange() {
        // expectedNumberOfPropertyFields represents the number of enum entries plus that static members, plus one for the $VALUES that every enum gets
        val expectedNumberOfPropertyFields = 11

        Assert.assertEquals("Declared properties in enum \"" +
                Property::class.java.simpleName +
                "\" have changed!  Ensure that changes are backwards compatible " +
                "and com.fisk.twig.config.PropertyTest2 has been updated appropriately.\n",
                expectedNumberOfPropertyFields.toLong(),
                Property::class.java.declaredFields.size.toLong())
    }

    @Test
    fun ensureAllPropertiesAreTested() {
        val properties = EnumSet.allOf(Property::class.java)

        for (propertyTestDefinition in PropertyNameTest.PROPERTY_TEST_DEFINITIONS) {
            properties.remove(propertyTestDefinition.property)
        }

        Assert.assertTrue("The following " + Property::class.java.simpleName + " entries do not have corresponding " +
                PropertyNameTest.PropertyTestDefinition::class.java.simpleName +
                " tests defined: " + properties.toString(),
                properties.isEmpty())
    }

    @Test
    fun testPropertyNameUniqueness() {
        val propertyNameStrings = HashSet<String>()

        for (property in Property.values()) {
            val propertyNameString = property.stringName
            Assert.assertFalse("Property string name \"$propertyNameString\" is not unique in Property",
                    propertyNameStrings.contains(propertyNameString))
            propertyNameStrings.add(propertyNameString)
        }
    }
}

package com.fisk.twig.reference

import com.fisk.twig.TwigFixtureTestBase
import com.fisk.twig.util.TwigTestUtils
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import junit.framework.Assert

class TwigVariableReferenceTest : TwigFixtureTestBase() {
    override fun getTestDataPath() = TwigTestUtils.BASE_TEST_DATA_PATH + "/reference/"

    fun testVariableReference1() {
        myFixture.configureByFiles("VariableReference1.twig")
        val element = myFixture.file.findElementAt(myFixture.caretOffset - 1)?.parent
        val refs = element?.references
        assertEquals(3, refs?.size)
    }

    fun testVariableReference2() {
        myFixture.configureByFiles("VariableReference2.twig")
        val element = myFixture.file.findElementAt(myFixture.caretOffset - 1)?.parent
        val refs = element?.references
        assertEquals(3, refs?.size)
    }

    fun testVariableReference3() {
        myFixture.configureByFiles("VariableReference3.twig")
        val element = myFixture.file.findElementAt(myFixture.caretOffset - 1)?.parent
        val refs = element?.references
        assertEquals(3, refs?.size)
    }

    fun testVariableReference4() {
        myFixture.configureByFiles("VariableReference4.twig")
        val element = myFixture.file.findElementAt(myFixture.caretOffset - 1)?.parent
        val refs = element?.references
        assertEquals(3, refs?.size)
    }
}
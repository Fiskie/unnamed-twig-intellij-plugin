package com.fisk.twig.ide.annotator

import com.fisk.twig.util.TwigTestUtils
import com.intellij.codeInspection.htmlInspections.HtmlUnknownTagInspection
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

class TwigHtmlInspectionTest : TwigAnnotatorTest() {
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        enableInspections()
    }

//    fun testUncompletedTag() {
//        checkErrors()
//    }

    fun testUncompletedTagInTwig() {
        checkErrors()
    }

    private fun enableInspections() {
        myFixture.enableInspections(HtmlUnknownTagInspection::class.java)
    }
}

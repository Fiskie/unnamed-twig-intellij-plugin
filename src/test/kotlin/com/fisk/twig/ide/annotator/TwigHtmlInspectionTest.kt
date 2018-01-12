package com.fisk.twig.ide.annotator

import com.intellij.codeInspection.htmlInspections.HtmlUnknownTagInspection

class TwigHtmlInspectionTest : TwigAnnotatorTestBase() {
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

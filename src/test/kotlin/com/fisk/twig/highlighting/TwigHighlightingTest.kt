package com.fisk.twig.highlighting

import com.fisk.twig.util.TwigTestUtils
import com.intellij.codeInspection.htmlInspections.HtmlUnknownTagInspection
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

class TwigHighlightingTest : LightPlatformCodeInsightFixtureTestCase() {
    override fun getBasePath(): String {
        return "/highlighting"
    }

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        enableInspections()
    }

    override fun getTestDataPath(): String {
        return TwigTestUtils.BASE_TEST_DATA_PATH + basePath
    }

    private fun doTest(extension: String) {
        myFixture.configureByFile(getTestName(true) + "." + extension)
        myFixture.checkHighlighting(true, false, true)
    }

    fun testUncompletedTag() {
        doTest("twig")
    }

    fun testUncompletedTagInTwig() {
        doTest("twig")
    }

    private fun enableInspections() {
        myFixture.enableInspections(HtmlUnknownTagInspection::class.java)
    }
}

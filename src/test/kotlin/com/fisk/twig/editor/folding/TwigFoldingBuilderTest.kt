package com.fisk.twig.editor.folding

import com.fisk.twig.util.TwigTestUtils
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

import java.io.File

class TwigFoldingBuilderTest : LightPlatformCodeInsightFixtureTestCase() {
    fun testCommentFolds() {
        doTest()
    }

    fun testEmptyCommentFold() {
        doTest()
    }

    /**
     * Test folding based by validating against a the file in [.TEST_DATA_PATH] who
     * names matches the test.<br></br>
     * <br></br>
     * Test data files contain &lt;form&gt; and &lt;/form&gt; tags to indictate the beginning and end
     * of expected folded areas
     */
    private fun doTest() {
        myFixture.testFolding(File(TEST_DATA_PATH, getTestName(true) + ".twig").absolutePath)
    }

    companion object {
        private val TEST_DATA_PATH = File(TwigTestUtils.BASE_TEST_DATA_PATH, "folding").absolutePath
    }
}

package com.fisk.twig.ide.actions

import com.fisk.twig.util.TwigTestUtils
import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

class TwigMoverTest : LightPlatformCodeInsightFixtureTestCase() {
    fun testMoveHtmlText() {
        doTest("twig")
    }

    fun testMoveHtmlBlock() {
        doTest("twig")
    }

    fun testMoveTwigExpression() {
        doTest("twig")
    }

    fun testMoveTwigStatementBlock() {
        doTest("twig")
    }

    private fun doTest(ext: String) {
        myFixture.configureByFile(getTestName(false) + "." + ext)
        myFixture.performEditorAction(IdeActions.ACTION_MOVE_STATEMENT_UP_ACTION)
        myFixture.checkResultByFile(getTestName(false) + ".after." + ext)
    }

    override fun getTestDataPath(): String {
        return TwigTestUtils.BASE_TEST_DATA_PATH + "/mover"
    }
}

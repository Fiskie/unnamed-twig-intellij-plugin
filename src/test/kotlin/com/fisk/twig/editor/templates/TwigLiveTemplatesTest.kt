package com.fisk.twig.editor.templates

import com.fisk.twig.util.TwigTestUtils
import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.editor.actionSystem.EditorAction
import com.intellij.openapi.util.io.FileUtil
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase


class TwigLiveTemplatesTest internal constructor() : LightPlatformCodeInsightFixtureTestCase() {
    override fun getTestDataPath() = TwigTestUtils.BASE_TEST_DATA_PATH + basePath

    override fun getBasePath(): String {
        return FileUtil.toSystemDependentName("/liveTemplates/")
    }

    private fun doTest() {
        myFixture.configureByFiles(getTestName(true) + ".twig")
        expandTemplate()
        myFixture.checkResultByFile(getTestName(true) + ".after.twig")
    }

    fun testBlk() {
        doTest()
    }

    fun testSpl() {
        doTest()
    }

    private fun expandTemplate() {
        val action = ActionManager.getInstance().getAction(IdeActions.ACTION_EXPAND_LIVE_TEMPLATE_BY_TAB) as EditorAction
        action.actionPerformed(myFixture.editor, DataManager.getInstance().dataContext)
    }
}

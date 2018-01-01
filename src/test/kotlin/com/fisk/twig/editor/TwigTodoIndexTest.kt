package com.fisk.twig.editor

import com.fisk.twig.util.TwigTestUtils
import com.intellij.psi.search.PsiTodoSearchHelper
import com.intellij.testFramework.LightPlatformCodeInsightTestCase

class TwigTodoIndexTest : LightPlatformCodeInsightTestCase() {
    override fun getTestDataPath() = TwigTestUtils.BASE_TEST_DATA_PATH + "/todo/"

    fun testNoTodo() {
        checkTodoCount(0)
    }

    fun testFileWithTwoTodo() {
        checkTodoCount(2)
    }

    fun testHtmlTodoOnly() {
        checkTodoCount(1)
    }

    private fun checkTodoCount(expectedTodoCount: Int) {
        configureByFile(getTestName(true) + ".twig")
        val items = PsiTodoSearchHelper.SERVICE.getInstance(getProject()).findTodoItems(getFile())
        assertEquals(expectedTodoCount, items.size)
    }
}
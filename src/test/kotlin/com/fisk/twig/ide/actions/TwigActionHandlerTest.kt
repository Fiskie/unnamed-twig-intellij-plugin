package com.fisk.twig.ide.actions

import com.fisk.twig.file.TwigFileType
import com.intellij.codeInsight.generation.CommentByBlockCommentHandler
import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.editor.actionSystem.EditorActionManager
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.project.Project
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

abstract class TwigActionHandlerTest : LightPlatformCodeInsightFixtureTestCase() {
    private fun performWriteAction(project: Project, action: Runnable) {
        ApplicationManager.getApplication().runWriteAction { CommandProcessor.getInstance().executeCommand(project, action, "test command", null) }
    }

    private fun validateTestStrings(before: String, expected: String) {
        if (!before.contains("<caret>") || !expected.contains("<caret>")) {
            throw IllegalArgumentException("Test strings must contain \"<caret>\" to indicate caret position")
        }
    }

    /**
     * Call this method to test behavior when the given charToType is typed at the &lt;caret&gt;.
     * See class documentation for more info: [TwigActionHandlerTest]
     */
    internal fun doCharTest(charToType: Char, before: String, expected: String) {
        val typedAction = EditorActionManager.getInstance().typedAction
        doExecuteActionTest(before, expected, Runnable { typedAction.actionPerformed(myFixture.editor, charToType, (myFixture.editor as EditorEx).dataContext) })
    }

    /**
     * Call this method to test behavior when Enter is typed.
     * See class documentation for more info: [TwigActionHandlerTest]
     */
    protected fun doEnterTest(before: String, expected: String) {
        val enterActionHandler = EditorActionManager.getInstance().getActionHandler(IdeActions.ACTION_EDITOR_ENTER)
        doExecuteActionTest(before, expected,
                Runnable { enterActionHandler.execute(myFixture.editor, (myFixture.editor as EditorEx).dataContext) })
    }

    /**
     * Call this method to test behavior when the "Comment with Line Comment" action is executed.
     * See class documentation for more info: [TwigActionHandlerTest]
     */
    internal fun doLineCommentTest(before: String, expected: String) {
        doExecuteActionTest(before, expected,
                Runnable { PlatformTestUtil.invokeNamedAction(IdeActions.ACTION_COMMENT_LINE) })
    }

    /**
     * Call this method to test behavior when the "Comment with Block Comment" action is executed.
     * See class documentation for more info: [TwigActionHandlerTest]
     */
    internal fun doBlockCommentTest(before: String, expected: String) {
        doExecuteActionTest(before, expected, Runnable {
            CommentByBlockCommentHandler().invoke(myFixture.project, myFixture.editor,
                    myFixture.editor.caretModel.primaryCaret, myFixture.file)
        })
    }

    private fun doExecuteActionTest(before: String, expected: String, action: Runnable) {
        validateTestStrings(before, expected)

        myFixture.configureByText(TwigFileType.INSTANCE, before)
        performWriteAction(myFixture.project, action)
        myFixture.checkResult(expected)
    }
}
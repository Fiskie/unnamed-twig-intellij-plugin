package com.fisk.twig.editor.actions

import com.fisk.twig.parsing.TwigTokenTypes
import com.fisk.twig.psi.TwigBlockWrapper
import com.fisk.twig.psi.TwigPsiFile
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiFile

class TwigEnterHandler : EnterHandlerDelegateAdapter() {
    override fun preprocessEnter(file: PsiFile, editor: Editor, caretOffset: Ref<Int>, caretAdvance: Ref<Int>, dataContext: DataContext, originalHandler: EditorActionHandler?): EnterHandlerDelegate.Result {
        if (file is TwigPsiFile && isBetweenTwigBlockStatementTags(editor, file, caretOffset.get())) {
            originalHandler?.execute(editor, editor.caretModel.currentCaret, dataContext)
            return EnterHandlerDelegate.Result.Default
        }

        return EnterHandlerDelegate.Result.Continue
    }

    private fun isBetweenTwigBlockStatementTags(editor: Editor, file: PsiFile, offset: Int): Boolean {
        val prevChar = file.findElementAt(offset - 1)
        val nextChar = file.findElementAt(offset + 1)

        if (prevChar == null || nextChar == null) {
            return false
        }

        if (prevChar.node.elementType != TwigTokenTypes.STATEMENT_CLOSE) {
            return false
        }

        if (nextChar.node.elementType != TwigTokenTypes.STATEMENT_OPEN) {
            return false
        }

        // todo: probably something

        return true
    }
}
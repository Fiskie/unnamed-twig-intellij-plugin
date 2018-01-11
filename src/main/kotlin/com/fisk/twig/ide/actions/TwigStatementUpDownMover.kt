package com.fisk.twig.ide.actions

import com.intellij.codeInsight.editorActions.moveUpDown.StatementUpDownMover
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

class TwigStatementUpDownMover : StatementUpDownMover() {
    override fun checkAvailable(editor: Editor, file: PsiFile, info: MoveInfo, down: Boolean): Boolean {
        return true
    }


}
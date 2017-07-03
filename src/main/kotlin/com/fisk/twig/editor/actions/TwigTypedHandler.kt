package com.fisk.twig.editor.actions

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class TwigTypedHandler : TypedHandlerDelegate() {
    override fun beforeCharTyped(c: Char, project: Project?, editor: Editor?, file: PsiFile?, fileType: FileType?): Result {
        return super.beforeCharTyped(c, project, editor, file, fileType)
    }

    override fun checkAutoPopup(charTyped: Char, project: Project?, editor: Editor?, file: PsiFile?): Result {
        return super.checkAutoPopup(charTyped, project, editor, file)
    }

    override fun charTyped(c: Char, project: Project?, editor: Editor, file: PsiFile): Result {
        return super.charTyped(c, project, editor, file)
    }
}
package com.fisk.twig.structure

import com.fisk.twig.psi.*
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.TextEditorBasedStructureViewModel
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

internal class TwigStructureViewModel(psiFile: PsiFile, editor: Editor?) : TextEditorBasedStructureViewModel(editor, psiFile) {
    override fun getSuitableClasses() = ourSuitableClasses

    override fun getRoot() = TwigTreeElementFile(psiFile as TwigPsiFile)

    companion object {
        val ourSuitableClasses = arrayOf<Class<*>>(TwigBlockWrapper::class.java)
    }
}

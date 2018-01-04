package com.fisk.twig.structure

import com.fisk.twig.psi.TwigPsiFile
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase

internal class TwigTreeElementFile(private val myFile: TwigPsiFile) : PsiTreeElementBase<TwigPsiFile>(myFile) {
    override fun getChildrenBase() = TwigTreeElement.getStructureViewTreeElements(myFile)

    override fun getPresentableText() = myFile.name
}

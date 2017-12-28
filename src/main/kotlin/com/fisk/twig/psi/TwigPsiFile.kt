package com.fisk.twig.psi

import com.fisk.twig.TwigLanguage
import com.fisk.twig.file.TwigFileType
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.lang.Language
import com.intellij.psi.FileViewProvider

class TwigPsiFile(
        viewProvider: FileViewProvider,
        val lang: Language = TwigLanguage.INSTANCE
) : PsiFileBase(viewProvider, lang) {
    override fun getFileType() = TwigFileType.INSTANCE
    override fun toString() = "Twig: " + name
}
package com.fisk.twig.file

import com.fisk.twig.TwigLanguage
import com.intellij.lang.Language
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.FileViewProvider
import com.intellij.psi.FileViewProviderFactory
import com.intellij.psi.PsiManager

class TwigFileViewProviderFactory : FileViewProviderFactory {
    override fun createFileViewProvider(virtualFile: VirtualFile,
                                        language: Language,
                                        psiManager: PsiManager,
                                        eventSystemEnabled: Boolean): FileViewProvider {
        assert(language.isKindOf(TwigLanguage.INSTANCE))
        return TwigFileViewProvider(psiManager, virtualFile, eventSystemEnabled, language)
    }
}


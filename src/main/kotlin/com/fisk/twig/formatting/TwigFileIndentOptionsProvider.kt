package com.fisk.twig.formatting

import com.fisk.twig.psi.TwigPsiFile
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.FileIndentOptionsProvider
import com.intellij.psi.templateLanguages.TemplateLanguageFileViewProvider

class TwigFileIndentOptionsProvider : FileIndentOptionsProvider() {
    override fun getIndentOptions(settings: CodeStyleSettings, file: PsiFile) = when (file) {
        is TwigPsiFile -> {
            val language = (file.viewProvider as TemplateLanguageFileViewProvider).templateDataLanguage
            settings.getCommonSettings(language).indentOptions
        }
        else -> null
    }
}

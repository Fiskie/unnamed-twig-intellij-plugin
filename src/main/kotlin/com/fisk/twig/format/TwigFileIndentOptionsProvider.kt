package com.fisk.twig.format

import com.fisk.twig.TwigLanguage
import com.fisk.twig.psi.TwigPsiFile
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.FileIndentOptionsProvider
import com.intellij.psi.templateLanguages.TemplateLanguageFileViewProvider

class TwigFileIndentOptionsProvider : FileIndentOptionsProvider() {
    override fun getIndentOptions(settings: CodeStyleSettings, file: PsiFile) = when (file) {
        is TwigPsiFile -> settings.getCommonSettings(TwigLanguage.INSTANCE).indentOptions
        else -> null
    }
}

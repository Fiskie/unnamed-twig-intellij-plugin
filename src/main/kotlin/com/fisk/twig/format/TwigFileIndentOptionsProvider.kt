package com.fisk.twig.format

import com.fisk.twig.psi.TwigPsiFile
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.FileIndentOptionsProvider
import com.intellij.psi.templateLanguages.TemplateLanguageFileViewProvider

class TwigFileIndentOptionsProvider : FileIndentOptionsProvider() {
    override fun getIndentOptions(settings: CodeStyleSettings, file: PsiFile): CommonCodeStyleSettings.IndentOptions? {
        if (file is TwigPsiFile) {
            if (file.viewProvider is TemplateLanguageFileViewProvider) {
                val language = (file.viewProvider as TemplateLanguageFileViewProvider).templateDataLanguage
                return settings.getCommonSettings(language).indentOptions
            }
        }
        return null
    }
}

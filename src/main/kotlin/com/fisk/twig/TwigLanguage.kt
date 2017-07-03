package com.fisk.twig

import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.StdFileTypes
import com.intellij.psi.templateLanguages.TemplateLanguage
import org.jetbrains.annotations.NonNls

class TwigLanguage : Language, TemplateLanguage {
    constructor() : super("Twig", "text/html")

    constructor(baseLanguage: Language?, @NonNls ID: String, @NonNls vararg mimeTypes: String) : super(baseLanguage, ID, *mimeTypes)

    companion object {
        val INSTANCE = TwigLanguage()

        // ideally this would be public static, but the static inits in the tests get cranky when we do that
        val defaultTemplateLang: LanguageFileType
            get() = StdFileTypes.HTML
    }
}

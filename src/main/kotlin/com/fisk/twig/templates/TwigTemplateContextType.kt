package com.fisk.twig.templates


import com.fisk.twig.highlighting.TwigHighlighter
import com.fisk.twig.TwigLanguage
import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.psi.PsiFile

class TwigTemplateContextType private constructor() : TemplateContextType("Twig", "Twig") {
    override fun isInContext(file: PsiFile, offset: Int): Boolean {
        return TwigLanguage.INSTANCE.`is`(file.language)
    }

    override fun createHighlighter() = TwigHighlighter()
}

package com.fisk.twig.editor.templates


import com.fisk.twig.TwigHighlighter
import com.fisk.twig.TwigLanguage
import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.psi.PsiFile

class TwigTemplateContextType protected constructor() : TemplateContextType("Twig", "Twig") {

    override fun isInContext(file: PsiFile, offset: Int): Boolean {
        return TwigLanguage.INSTANCE.`is`(file.language)
    }

    override fun createHighlighter(): SyntaxHighlighter? {
        return TwigHighlighter()
    }
}

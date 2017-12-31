package com.fisk.twig.inspections


import com.fisk.twig.file.TwigFileViewProvider
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_OPEN
import com.intellij.codeInsight.highlighting.TemplateLanguageErrorFilter
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.templateLanguages.TemplateLanguageFileViewProvider
import com.intellij.psi.tree.TokenSet

class TwigErrorFilter protected constructor() : TemplateLanguageErrorFilter(START_TEMPLATE_TOKENS, TwigFileViewProvider::class.java, "HTML") {
    override fun shouldIgnoreErrorAt(viewProvider: FileViewProvider, offset: Int): Boolean {
        return if (super.shouldIgnoreErrorAt(viewProvider, offset)) true else hasWhitespacesInHtmlBetweenErrorAndOpenTokens(offset, viewProvider as TemplateLanguageFileViewProvider)

    }

    companion object {
        private val START_TEMPLATE_TOKENS = TokenSet.create(STATEMENT_OPEN)

        private fun hasWhitespacesInHtmlBetweenErrorAndOpenTokens(offset: Int, viewProvider: TemplateLanguageFileViewProvider): Boolean {
            val at = viewProvider.findElementAt(offset, viewProvider.templateDataLanguage) as? PsiWhiteSpace ?: return false
            val elementAt = viewProvider.findElementAt(at.textRange.endOffset + 1, viewProvider.baseLanguage)
            return elementAt != null && START_TEMPLATE_TOKENS.contains(elementAt.node.elementType)

        }
    }
}

package com.fisk.twig.formatting

import com.fisk.twig.parsing.TwigTokenTypes
import com.intellij.formatting.Alignment
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.Wrap
import com.intellij.formatting.templateLanguages.DataLanguageBlockWrapper
import com.intellij.formatting.templateLanguages.TemplateLanguageBlock
import com.intellij.formatting.templateLanguages.TemplateLanguageFormattingModelBuilder
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.DocumentBasedFormattingModel
import com.intellij.psi.formatter.FormattingDocumentModelImpl
import com.intellij.psi.formatter.xml.HtmlPolicy
import com.intellij.psi.templateLanguages.SimpleTemplateLanguageFormattingModelBuilder

class TwigFormattingModelBuilder : TemplateLanguageFormattingModelBuilder() {
    override fun createTemplateLanguageBlock(node: ASTNode, wrap: Wrap?, alignment: Alignment?, foreignChildren: MutableList<DataLanguageBlockWrapper>?, codeStyleSettings: CodeStyleSettings): TemplateLanguageBlock {
        val model = FormattingDocumentModelImpl.createOn(node.psi.containingFile)
        val policy = HtmlPolicy(codeStyleSettings, model)

        return if (TwigTokenTypes.TAGS.contains(node.elementType)) {
            TwigTagLanguageBlock(node, wrap, alignment, this, codeStyleSettings, foreignChildren, policy)
        } else {
            TwigBlockLanguageBlock(node, wrap, alignment, this, codeStyleSettings, foreignChildren, policy)
        }
    }

    /**
     * We have to override [com.intellij.formatting.templateLanguages.TemplateLanguageFormattingModelBuilder.createModel]
     * since after we delegate to some templated languages, those languages (xml/html for sure, potentially others)
     * delegate right back to us to format the TwigTokenTypes.OUTER_ELEMENT_TYPE token we tell them to ignore,
     * causing an stack-overflowing loop of polite format-delegation.
     */
    override fun createModel(element: PsiElement, settings: CodeStyleSettings): FormattingModel {
        val node = element.node

        return if (node.elementType == TwigTokenTypes.OUTER_ELEMENT_TYPE) {
            // If we're looking at a TwigTokenTypes.OUTER_ELEMENT_TYPE element, then we've been invoked by our templated
            // language.  Make a dummy block to allow that formatter to continue
            SimpleTemplateLanguageFormattingModelBuilder().createModel(element, settings)
        } else {
            val file = element.containingFile
            val rootBlock = getRootBlock(file, file.viewProvider, settings)
            DocumentBasedFormattingModel(rootBlock, element.project, settings, file.fileType, file)
        }
    }

    override fun dontFormatMyModel() = false
}
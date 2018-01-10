package com.fisk.twig.format

import com.fisk.twig.parsing.TwigTokenTypes
import com.intellij.formatting.*
import com.intellij.formatting.templateLanguages.DataLanguageBlockWrapper
import com.intellij.formatting.templateLanguages.TemplateLanguageBlockFactory
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.formatter.xml.HtmlPolicy

class TwigTagLanguageBlock(node: ASTNode,
                           wrap: Wrap?,
                           alignment: Alignment?,
                           blockFactory: TemplateLanguageBlockFactory,
                           settings: CodeStyleSettings,
                           foreignChildren: List<DataLanguageBlockWrapper>?,
                           htmlPolicy: HtmlPolicy) : TwigBlockLanguageBlock(node, wrap, alignment, blockFactory, settings, foreignChildren, htmlPolicy) {

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        return null
    }
}
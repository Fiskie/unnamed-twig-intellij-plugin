package com.fisk.twig.format

import com.fisk.twig.parsing.TwigTokenTypes
import com.intellij.formatting.Alignment
import com.intellij.formatting.ChildAttributes
import com.intellij.formatting.Wrap
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

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        if (newChildIndex > 0) {
            val blocks = subBlocks
            if (blocks.size > newChildIndex - 1) {
                val prevBlock = blocks[newChildIndex - 1]
                if (prevBlock is AbstractBlock) {
                    val node = prevBlock.node

                    if (node.elementType === TwigTokenTypes.TAG) {
                        return ChildAttributes(null, prevBlock.getAlignment())
                    }
                }
            }
        }

        return super.getChildAttributes(newChildIndex)
    }
}
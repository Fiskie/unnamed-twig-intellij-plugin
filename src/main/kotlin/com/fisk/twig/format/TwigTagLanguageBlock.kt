package com.fisk.twig.format

import com.fisk.twig.ide.pages.TwigCodeStyleSettings
import com.fisk.twig.parsing.TwigTokenTypes
import com.fisk.twig.psi.TwigTag
import com.fisk.twig.psi.util.TwigBraceUtil
import com.intellij.formatting.*
import com.intellij.formatting.templateLanguages.DataLanguageBlockWrapper
import com.intellij.formatting.templateLanguages.TemplateLanguageBlockFactory
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
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
        if (child1 is ASTBlock && child2 is ASTBlock) {
            when (child1.node.elementType) {
                TwigTokenTypes.EXPRESSION_OPEN -> return getExpressionBraceSpacing()
                TwigTokenTypes.STATEMENT_OPEN -> return getStatementBraceSpacing()
            }

            when (child2.node.elementType) {
                TwigTokenTypes.EXPRESSION_CLOSE -> return getExpressionBraceSpacing()
                TwigTokenTypes.STATEMENT_CLOSE -> return getStatementBraceSpacing()
            }
        }

        return null
    }

    private fun getExpressionBraceSpacing(): Spacing {
        val twigSettings = settings.getCustomSettings(TwigCodeStyleSettings::class.java)

        if (twigSettings.SPACES_IN_EXPRESSION_TAGS) {
            return Spacing.createSpacing(1, 1,0, false, 1)
        } else {
            return Spacing.createSpacing(0, 0, 0, false, 1)
        }
    }

    private fun getStatementBraceSpacing(): Spacing {
        val twigSettings = settings.getCustomSettings(TwigCodeStyleSettings::class.java)

        if (twigSettings.SPACES_IN_STATEMENT_TAGS) {
            return Spacing.createSpacing(1, 1,0, false, 1)
        } else {
            return Spacing.createSpacing(0, 0, 0, false, 1)
        }
    }
}
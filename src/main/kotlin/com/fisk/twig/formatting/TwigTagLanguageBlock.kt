package com.fisk.twig.formatting

import com.fisk.twig.TwigLanguage
import com.fisk.twig.formatting.settings.TwigCodeStyleSettings
import com.fisk.twig.parsing.TwigTokenTypes
import com.intellij.formatting.*
import com.intellij.formatting.templateLanguages.DataLanguageBlockWrapper
import com.intellij.formatting.templateLanguages.TemplateLanguageBlockFactory
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.formatter.xml.HtmlPolicy

class TwigTagLanguageBlock(node: ASTNode,
                           wrap: Wrap?,
                           alignment: Alignment?,
                           blockFactory: TemplateLanguageBlockFactory,
                           settings: CodeStyleSettings,
                           foreignChildren: List<DataLanguageBlockWrapper>?,
                           htmlPolicy: HtmlPolicy) : TwigBlockLanguageBlock(node, wrap, alignment, blockFactory, settings, foreignChildren, htmlPolicy) {

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        val commonSettings = settings
        val twigSettings = settings.getCustomSettings(TwigCodeStyleSettings::class.java)

        if (child1 is ASTBlock && child2 is ASTBlock) {
            when (child1.node.elementType) {
                TwigTokenTypes.EXPRESSION_OPEN -> return getCommonExpressionSpacing(twigSettings.SPACES_IN_EXPRESSION_TAGS)
                TwigTokenTypes.STATEMENT_OPEN -> return getCommonExpressionSpacing(twigSettings.SPACES_IN_STATEMENT_TAGS)
                TwigTokenTypes.OPERATOR -> return getCommonExpressionSpacing(commonSettings.SPACE_AROUND_ADDITIVE_OPERATORS)
                TwigTokenTypes.COMMA -> return getCommonExpressionSpacing(commonSettings.SPACE_AFTER_COMMA)
            }

            when (child2.node.elementType) {
                TwigTokenTypes.EXPRESSION_CLOSE -> return getCommonExpressionSpacing(twigSettings.SPACES_IN_EXPRESSION_TAGS)
                TwigTokenTypes.STATEMENT_CLOSE -> return getCommonExpressionSpacing(twigSettings.SPACES_IN_STATEMENT_TAGS)
                TwigTokenTypes.COMMA -> return getCommonExpressionSpacing(commonSettings.SPACE_BEFORE_COMMA)
            }
        }

        return null
    }

    private fun getCommonExpressionSpacing(enabled: Boolean): Spacing = when (enabled) {
        true -> Spacing.createSpacing(1, 1, 0, false, 1)
        false -> Spacing.createSpacing(0, 0, 0, false, 1)
    }
}
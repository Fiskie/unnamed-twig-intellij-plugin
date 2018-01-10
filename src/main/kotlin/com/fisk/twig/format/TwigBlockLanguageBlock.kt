package com.fisk.twig.format

import com.fisk.twig.parsing.TwigTokenTypes
import com.fisk.twig.psi.TwigPsiElement
import com.fisk.twig.psi.util.TwigPsiUtil
import com.intellij.formatting.Alignment
import com.intellij.formatting.ChildAttributes
import com.intellij.formatting.Indent
import com.intellij.formatting.Wrap
import com.intellij.formatting.templateLanguages.BlockWithParent
import com.intellij.formatting.templateLanguages.DataLanguageBlockWrapper
import com.intellij.formatting.templateLanguages.TemplateLanguageBlock
import com.intellij.formatting.templateLanguages.TemplateLanguageBlockFactory
import com.intellij.lang.ASTNode
import com.intellij.lang.javascript.types.JSEmbeddedContentElementType
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.xml.AnotherLanguageBlockWrapper
import com.intellij.psi.formatter.xml.HtmlPolicy
import com.intellij.psi.formatter.xml.SyntheticBlock
import com.intellij.psi.xml.XmlTag


open class TwigBlockLanguageBlock(
        node: ASTNode,
        wrap: Wrap?,
        alignment: Alignment?,
        blockFactory: TemplateLanguageBlockFactory,
        settings: CodeStyleSettings,
        foreignChildren: List<DataLanguageBlockWrapper>?,
        val htmlPolicy: HtmlPolicy
) : TemplateLanguageBlock(
        node, wrap, alignment, blockFactory, settings, foreignChildren
) {
    override fun getTemplateTextElementType() = TwigTokenTypes.CONTENT

    override fun isRequiredRange(range: TextRange?) = false

    /**
     * Returns this block's first "real" foreign block parent if it exists, and null otherwise.  (By "real" here, we mean that this method
     * skips SyntheticBlock blocks inserted by the template formatter)
     *
     * @param immediate Pass true to only check for an immediate foreign parent, false to look up the hierarchy.
     */
    private fun getForeignBlockParent(immediate: Boolean): DataLanguageBlockWrapper? {
        var foreignBlockParent: DataLanguageBlockWrapper? = null
        var parent: BlockWithParent? = parent

        while (parent != null) {
            if (parent is DataLanguageBlockWrapper && parent.original !is SyntheticBlock) {
                foreignBlockParent = parent
                break
            } else if (immediate && parent is TwigBlockLanguageBlock) {
                break
            }
            parent = parent.parent
        }

        return foreignBlockParent
    }

    private fun getIndentOfNonRootBlockElement(): Indent {
        // we're computing the indent for a non-root block:
        //      if it's not contained in a foreign block, indent!
        val foreignBlockParent = getForeignBlockParent(false) ?: return Indent.getNormalIndent()

        // otherwise, only indent if our foreign parent isn't indenting us
        if (foreignBlockParent.node is XmlTag) {
            val xmlTag = foreignBlockParent.node as XmlTag
            if (!htmlPolicy.indentChildrenOf(xmlTag)) {
                // no indent from xml parent, add our own
                return Indent.getNormalIndent()
            }
        }

        if (foreignBlockParent.original is AnotherLanguageBlockWrapper) {
            return Indent.getNormalIndent()
        }

        return Indent.getNoneIndent()
    }

    private fun getIndentOfForeignBlockParent(): Indent {
        // any element that is the direct descendant of a foreign block gets an indent
        // (unless that foreign element has been configured to not indent its children)
        val foreignParent = getForeignBlockParent(true)

        return if (foreignParent != null) {
            if (foreignParent.node is XmlTag && !htmlPolicy.indentChildrenOf(foreignParent.node as XmlTag)) {
                Indent.getNoneIndent()
            } else if (foreignParent.original is AnotherLanguageBlockWrapper) {
                Indent.getNoneIndent()
            } else {
                Indent.getNormalIndent()
            }
        } else {
            Indent.getNoneIndent()
        }
    }

    override fun getIndent(): Indent? {
        // ignore whitespace
        if (myNode.text.trim().isEmpty()) {
            return Indent.getNoneIndent()
        }

        if (TwigPsiUtil.isNonRootBlockElement(myNode.psi)) {
            return getIndentOfNonRootBlockElement()
        }

        if (myNode.treeParent != null && TwigPsiUtil.isNonRootBlockElement(myNode.treeParent.psi)) {
            // we're computing the indent for a direct descendant of a non-root block:
            //      if its Block parent (i.e. not Twig AST Tree parent) is a Twig block
            //      which has NOT been indented, then have the element provide the indent itself
            if (parent is TwigBlockLanguageBlock && (parent as TwigBlockLanguageBlock).indent === Indent.getNoneIndent()) {
                return Indent.getNormalIndent()
            }
        }

        return getIndentOfForeignBlockParent()
    }

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        return if (myNode.elementType === TwigTokenTypes.BLOCK_WRAPPER || (parent is DataLanguageBlockWrapper && (myNode.elementType != TwigTokenTypes.BLOCK || myNode.treeNext is PsiErrorElement))) {
            ChildAttributes(Indent.getNormalIndent(), null)
        } else {
            ChildAttributes(Indent.getNoneIndent(), null)
        }
    }
}
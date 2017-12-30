package com.fisk.twig.format

import com.fisk.twig.parsing.TwigTokenTypes
import com.fisk.twig.psi.TwigPsiUtil
import com.intellij.formatting.Alignment
import com.intellij.formatting.Indent
import com.intellij.formatting.Wrap
import com.intellij.formatting.templateLanguages.*
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.FormattingDocumentModelImpl
import com.intellij.psi.formatter.xml.HtmlPolicy
import com.intellij.psi.formatter.xml.SyntheticBlock
import com.intellij.psi.tree.IElementType
import com.intellij.psi.xml.XmlTag

class TwigFormattingModelBuilder : TemplateLanguageFormattingModelBuilder() {
    override fun createTemplateLanguageBlock(node: ASTNode, wrap: Wrap?, alignment: Alignment?, foreignChildren: MutableList<DataLanguageBlockWrapper>?, codeStyleSettings: CodeStyleSettings): TemplateLanguageBlock {
        val model = FormattingDocumentModelImpl.createOn(node.psi.containingFile)
        val policy = HtmlPolicy(codeStyleSettings, model)
        return TwigBlock(node, wrap, alignment, this, codeStyleSettings, foreignChildren, policy)
    }

    class TwigBlock(
            node: ASTNode,
            wrap: Wrap?,
            alignment: Alignment?,
            blockFactory: TemplateLanguageBlockFactory,
            settings: CodeStyleSettings,
            foreignChildren: MutableList<DataLanguageBlockWrapper>?,
            val htmlPolicy: HtmlPolicy
    ) : TemplateLanguageBlock(
            node, wrap, alignment, blockFactory, settings, foreignChildren
    ) {


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
                } else if (immediate && parent is TwigBlock) {
                    break
                }
                parent = parent.parent
            }

            return foreignBlockParent
        }

        override fun getTemplateTextElementType(): IElementType {
            // we ignore CONTENT tokens since they get formatted by the templated language
            return TwigTokenTypes.CONTENT
        }

        private fun isExpression(child: ASTNode): Boolean {
            val type = child.elementType
            return type === TwigTokenTypes.EXPRESSION
        }
        
        override fun getIndent(): Indent? {
            // ignore whitespace
            if (myNode.text.trim { it <= ' ' }.isEmpty()) {
                return Indent.getNoneIndent()
            }

            if (isExpression(myNode)) {
                return null
            }

            if (TwigPsiUtil.isNonRootBlockElement(myNode.psi)) {
                // we're computing the indent for a non-root STATEMENTS:
                //      if it's not contained in a foreign block, indent!
                val foreignBlockParent = getForeignBlockParent(false) ?: return Indent.getNormalIndent()

                // otherwise, only indent if our foreign parent isn't indenting us
                if (foreignBlockParent.getNode() is XmlTag) {
                    val xmlTag = foreignBlockParent.getNode() as XmlTag
                    if (!htmlPolicy.indentChildrenOf(xmlTag)) {
                        // no indent from xml parent, add our own
                        return Indent.getNormalIndent()
                    }
                }

                return Indent.getNoneIndent()
            }

            if (myNode.treeParent != null && TwigPsiUtil.isNonRootBlockElement(myNode.treeParent.psi)) {
                // we're computing the indent for a direct descendant of a non-root STATEMENTS:
                //      if its Block parent (i.e. not HB AST Tree parent) is a Twig block
                //      which has NOT been indented, then have the element provide the indent itself
                if (parent is TwigBlock && (parent as TwigBlock).getIndent() === Indent.getNoneIndent()) {
                    return Indent.getNormalIndent()
                }
            }

            // any element that is the direct descendant of a foreign block gets an indent
            // (unless that foreign element has been configured to not indent its children)
            val foreignParent = getForeignBlockParent(true)
            return if (foreignParent != null) {
                if (foreignParent.getNode() is XmlTag && !htmlPolicy.indentChildrenOf(foreignParent.getNode() as XmlTag)) {
                    Indent.getNoneIndent()
                } else Indent.getNormalIndent()
            } else Indent.getNoneIndent()

        }
    }
}
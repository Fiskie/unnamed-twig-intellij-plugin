package com.fisk.twig.format

import com.fisk.twig.config.TwigConfig
import com.fisk.twig.parsing.TwigTokenTypes
import com.fisk.twig.psi.TwigPsiUtil
import com.intellij.formatting.*
import com.intellij.formatting.templateLanguages.*
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.DocumentBasedFormattingModel
import com.intellij.psi.formatter.FormattingDocumentModelImpl
import com.intellij.psi.formatter.xml.HtmlPolicy
import com.intellij.psi.formatter.xml.SyntheticBlock
import com.intellij.psi.templateLanguages.SimpleTemplateLanguageFormattingModelBuilder
import com.intellij.psi.xml.XmlTag

class TwigFormattingModelBuilder : TemplateLanguageFormattingModelBuilder() {
    override fun createTemplateLanguageBlock(node: ASTNode, wrap: Wrap?, alignment: Alignment?, foreignChildren: MutableList<DataLanguageBlockWrapper>?, codeStyleSettings: CodeStyleSettings): TemplateLanguageBlock {
        val model = FormattingDocumentModelImpl.createOn(node.psi.containingFile)
        val policy = HtmlPolicy(codeStyleSettings, model)
        return TwigIndentBlock(node, wrap, alignment, this, codeStyleSettings, foreignChildren, policy)
    }

    /**
     * We have to override [com.intellij.formatting.templateLanguages.TemplateLanguageFormattingModelBuilder.createModel]
     * since after we delegate to some templated languages, those languages (xml/html for sure, potentially others)
     * delegate right back to us to format the TwigTokenTypes.OUTER_ELEMENT_TYPE token we tell them to ignore,
     * causing an stack-overflowing loop of polite format-delegation.
     */
    override fun createModel(element: PsiElement, settings: CodeStyleSettings): FormattingModel {
        if (!TwigConfig.isFormattingEnabled) {
            // formatting is disabled, return the no-op formatter (note that this still delegates formatting
            // to the templated language, which lets the users manage that separately)
            return SimpleTemplateLanguageFormattingModelBuilder().createModel(element, settings)
        }

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

    class TwigIndentBlock(
            node: ASTNode,
            wrap: Wrap?,
            alignment: Alignment?,
            blockFactory: TemplateLanguageBlockFactory,
            settings: CodeStyleSettings,
            foreignChildren: MutableList<DataLanguageBlockWrapper>?,
            private val htmlPolicy: HtmlPolicy
    ) : TemplateLanguageBlock(
            node, wrap, alignment, blockFactory, settings, foreignChildren
    ) {
        override fun getTemplateTextElementType() = TwigTokenTypes.CONTENT

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
                } else if (immediate && parent is TwigIndentBlock) {
                    break
                }
                parent = parent.parent
            }

            return foreignBlockParent
        }

        override fun getIndent(): Indent? {
            // ignore whitespace
            if (myNode.text.trim { it <= ' ' }.isEmpty()) {
                return Indent.getNoneIndent()
            }

            if (TwigPsiUtil.isNonRootBlockElement(myNode.psi)) {
                // we're computing the indent for a non-root block:
                //      if it's not contained in a foreign block, indent!
                val foreignBlockParent = getForeignBlockParent(false)

                if (foreignBlockParent == null) {
                    return Indent.getNormalIndent()
                }

                // otherwise, only indent if our foreign parent isn't indenting us
                if (foreignBlockParent.node is XmlTag) {
                    val xmlTag = foreignBlockParent.node as XmlTag
                    if (!htmlPolicy.indentChildrenOf(xmlTag)) {
                        // no indent from xml parent, add our own
                        return Indent.getNormalIndent()
                    }
                }

                return Indent.getNoneIndent()
            }

            if (myNode.treeParent != null && TwigPsiUtil.isNonRootBlockElement(myNode.treeParent.psi)) {
                // we're computing the indent for a direct descendant of a non-root block:
                //      if its Block parent (i.e. not Twig AST Tree parent) is a Twig block
                //      which has NOT been indented, then have the element provide the indent itself
                if (parent is TwigIndentBlock && (parent as TwigIndentBlock).indent === Indent.getNoneIndent()) {
                    return Indent.getNormalIndent()
                }
            }

            // any element that is the direct descendant of a foreign block gets an indent
            // (unless that foreign element has been configured to not indent its children)
            val foreignParent = getForeignBlockParent(true)

            if (foreignParent != null) {
                if (foreignParent.node is XmlTag && !htmlPolicy.indentChildrenOf(foreignParent.node as XmlTag)) {
                    return Indent.getNoneIndent()
                } else {
                    return Indent.getNormalIndent()
                }
            } else {
                return Indent.getNoneIndent()
            }
        }

        override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
            return if (myNode.elementType === TwigTokenTypes.BLOCK_WRAPPER || parent is DataLanguageBlockWrapper) {
                ChildAttributes(Indent.getNormalIndent(), null)
            } else {
                ChildAttributes(Indent.getNoneIndent(), null)
            }
        }
    }
}
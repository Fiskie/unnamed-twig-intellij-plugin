package com.fisk.twig.editor.folding

import com.fisk.twig.config.TwigConfig
import com.fisk.twig.parsing.TwigTokenTypes
import com.fisk.twig.psi.TwigBlockEndStatement
import com.fisk.twig.psi.TwigBlockStartStatement
import com.fisk.twig.psi.TwigBlockWrapper
import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilder
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import java.util.*

class TwigFoldingBuilder : FoldingBuilder, DumbAware {
    override fun getPlaceholderText(node: ASTNode) = "..."

    override fun buildFoldRegions(node: ASTNode, document: Document): Array<FoldingDescriptor> {
        val descriptors = ArrayList<FoldingDescriptor>()
        addDescriptors(node.psi, document, descriptors)
        println("${descriptors.size} descriptors for el ${node.psi}")
        return descriptors.toTypedArray()
    }

    fun addDescriptors(psi: PsiElement, document: Document, descriptors: ArrayList<FoldingDescriptor>) {
        addCommentDescriptors(psi, document, descriptors)
        addBlockDescriptors(psi, document, descriptors)

        var child = psi.firstChild

        while (child != null) {
            addDescriptors(child, document, descriptors)
            child = child.nextSibling
        }
    }

    fun addCommentDescriptors(psi: PsiElement, document: Document, descriptors: ArrayList<FoldingDescriptor>) {
        if (isSingleLine(psi, document)) {
            return
        }

        if (psi.node.elementType == TwigTokenTypes.COMMENT) {
            if (psi.node.text.length > 6) {
                val textRange = psi.node.textRange
                val range = TextRange(textRange.startOffset + 2, textRange.endOffset - 2)
                descriptors.add(FoldingDescriptor(psi, range))
            }
        }
    }

    fun addBlockDescriptors(psi: PsiElement, document: Document, descriptors: ArrayList<FoldingDescriptor>) {
        if (psi !is TwigBlockWrapper) {
            return
        }

        val startCloseToken = getBlockStartCloseToken(psi.firstChild)
        val endCloseToken = getBlockEndCloseToken(psi.lastChild)

        println("Adding descriptors for block...")

        if (startCloseToken == null || endCloseToken == null) {
            println("Can't fold because missing start/close token(s)")
            return
        }

        val endOfFirstOpenLine = document.getLineEndOffset(document.getLineNumber(psi.textRange.startOffset))

        val foldStartOffset = Math.min(
                startCloseToken.textRange.startOffset,
                endOfFirstOpenLine
        )

        val foldEndOffset = endCloseToken.textRange.startOffset
        val range = TextRange(foldStartOffset, foldEndOffset)

        descriptors.add(FoldingDescriptor(psi, range))
    }

    private fun getBlockStartCloseToken(psi: PsiElement): PsiElement? {
        if (psi is TwigBlockStartStatement) {
            val closeToken = psi.lastChild
            if (closeToken?.node?.elementType == TwigTokenTypes.STATEMENT_CLOSE) {
                return closeToken
            }
        }

        return null
    }

    private fun getBlockEndCloseToken(psi: PsiElement): PsiElement? {
        if (psi is TwigBlockEndStatement) {
            val closeToken = psi.lastChild
            if (closeToken?.node?.elementType == TwigTokenTypes.STATEMENT_CLOSE) {
                return closeToken
            }
        }

        return null
    }

    private fun isSingleLine(psi: PsiElement, document: Document): Boolean {
        val range = psi.textRange
        return document.getLineNumber(range.startOffset) == document.getLineNumber(range.endOffset)
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return TwigConfig.isAutoCollapseBlocksEnabled
    }
}
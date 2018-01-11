package com.fisk.twig.ide.annotator

import com.fisk.twig.TwigBundle
import com.fisk.twig.parsing.TwigTokenTypes
import com.fisk.twig.psi.*
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

class TwigUnexpectedContentInChildTemplateAnnotator : Annotator {
    val ALLOWED_CONTENT_TAGS = setOf("block", "set")

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is TwigSimpleStatement && element.getTag()?.name == "extends") {
            markInvalidTokens(element.parent.parent as TwigPsiElement, holder)
        } else {
            if (element !is TwigBlockWrapper) {
                return
            }

            val startStatement = element.getStartStatement()
            val tag = startStatement?.getTag()

            if (tag?.name != "embed") {
                return
            }

            // We are looking at the block wrapper for an embed block
            val block = element.getContents()
            markInvalidTokens(block!!, holder)
        }
    }

    private fun markInvalidTokens(root: TwigPsiElement, holder: AnnotationHolder) {
        root.getAllChildren().forEach {
            if (it is TwigBlockWrapper) {
                // if blocks and whatever else are allowed inside embed, so recurse
                if (!ALLOWED_CONTENT_TAGS.contains(it.getStartStatement()?.getTag()?.name)) {
                    val content = it.getContents()

                    content?.let {
                        markInvalidTokens(content, holder)
                    }
                }
            } else if (it is TwigExpressionBlock) {
                createError(it, holder)
            } else if (it.node.elementType == TwigTokenTypes.CONTENT) {
                createError(it, holder)
            }
        }
    }

    private fun createError(element: PsiElement, holder: AnnotationHolder) {
        holder.createErrorAnnotation(element, TwigBundle.message("twig.unexpected.content.in.child"))
    }
}
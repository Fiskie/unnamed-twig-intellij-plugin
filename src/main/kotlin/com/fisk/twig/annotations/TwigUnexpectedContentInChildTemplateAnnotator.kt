package com.fisk.twig.annotations

import com.fisk.twig.TwigBundle
import com.fisk.twig.parsing.TwigTokenTypes
import com.fisk.twig.psi.TwigBlockWrapper
import com.fisk.twig.psi.TwigExpressionBlock
import com.fisk.twig.psi.TwigPsiElement
import com.fisk.twig.psi.TwigSimpleStatement
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement

class TwigUnexpectedContentInChildTemplateAnnotator : Annotator {
    val ALLOWED_CONTENT_TAGS = setOf("block", "set")

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is TwigSimpleStatement && element.tag?.name == "extends") {
            markInvalidTokens(element.parent.parent as TwigPsiElement, holder)
        } else {
            if (element !is TwigBlockWrapper) {
                return
            }

            val startStatement = element.getStartStatement()
            val tag = startStatement?.tag

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
                /*
                 * we can expect CONTENT within a block or set within an embed/extends,
                 * however we need to recurse through other statement blocks and
                 * check the CONTENT in those.
                 *
                 * This makes sure errors like {% embed %}{% if foo %}CONTENT...
                 * are flagged correctly.
                 */

                if (!ALLOWED_CONTENT_TAGS.contains(it.getStartStatement()?.tag?.name)) {
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
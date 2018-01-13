package com.fisk.twig.annotations

import com.fisk.twig.TwigBundle
import com.fisk.twig.psi.TwigBlockWrapper
import com.fisk.twig.psi.TwigInverseStatement
import com.fisk.twig.util.TwigTagUtil
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement

class TwigUnexpectedInverseTagAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is TwigBlockWrapper) {
            return
        }

        val tag = element.getStartStatement()?.tag?.name ?: return

        val canHaveInverseTag = TwigTagUtil.allowsInverseTag(tag)

        element.children.forEach {
            if (it is TwigInverseStatement && !canHaveInverseTag) {
                val message = TwigBundle.message("twig.unexpected.inverse.tag.inspection")
                holder.createErrorAnnotation(it.tag!!, message)
            }
        }
    }
}
package com.fisk.twig.annotations

import com.fisk.twig.TwigBundle
import com.fisk.twig.psi.TwigBlockWrapper
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace

/**
 * This annotation adds
 */
class TwigUnexpectedElementInSandboxTagAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is TwigBlockWrapper) {
            return
        }

        if (element.getStartStatement()?.tag?.name != "sandbox") {
            return
        }

        element.getContents()?.children?.forEach {
            if (it !is TwigBlockWrapper || it.getStartStatement()?.tag?.name != "include") {
                if (it !is PsiWhiteSpace) {
                    holder.createErrorAnnotation(it, TwigBundle.message("twig.unexpected.element.in.sandbox.inspection"))
                    return
                }
            }
        }
    }
}
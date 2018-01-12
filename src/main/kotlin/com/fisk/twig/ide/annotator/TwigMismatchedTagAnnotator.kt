package com.fisk.twig.ide.annotator

import com.fisk.twig.TwigBundle
import com.fisk.twig.TwigTagUtils
import com.fisk.twig.psi.TwigBlockWrapper
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement

class TwigMismatchedTagAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is TwigBlockWrapper) {
            return
        }

        val start = element.getStartStatement()
        val end = element.getEndStatement()

        if (start != null && end != null) {
            val startTag = start.getTag()
            val endTag = end.getTag()

            if (startTag != null && endTag != null && startTag.name != TwigTagUtils.normaliseTag(endTag.name!!)) {
                val document = element.containingFile.viewProvider.document!!

                val message = TwigBundle.message(
                        "twig.mismatched.tag.inspection",
                        startTag.name!!,
                        document.getLineNumber(startTag.textOffset) + 1
                )

                holder.createErrorAnnotation(endTag, message)
            }
        }
    }
}
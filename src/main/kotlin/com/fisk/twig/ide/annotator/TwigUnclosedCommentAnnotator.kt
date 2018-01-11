package com.fisk.twig.ide.annotator

import com.fisk.twig.TwigBundle
import com.fisk.twig.parsing.TwigTokenTypes.UNCLOSED_COMMENT
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement

class TwigUnclosedCommentAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element.node.elementType == UNCLOSED_COMMENT) {
            holder.createErrorAnnotation(element, TwigBundle.message("twig.unclosed.comment.inspection"))
        }
    }
}
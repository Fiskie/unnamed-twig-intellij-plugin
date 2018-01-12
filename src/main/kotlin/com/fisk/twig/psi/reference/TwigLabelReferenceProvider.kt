package com.fisk.twig.psi.reference

import com.fisk.twig.parsing.TwigTokenTypes
import com.fisk.twig.psi.impl.TwigVariableImpl
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.tree.TokenSet
import com.intellij.util.ProcessingContext

class TwigLabelReferenceProvider : PsiReferenceProvider() {
    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
        if (element !is TwigVariableImpl) {
            return PsiReference.EMPTY_ARRAY
        }

        val labelSet = TokenSet.create(TwigTokenTypes.LABEL)
        var textNode = element.node.findChildByType(labelSet)

        if (textNode != null) {
            textNode = textNode.treeNext
            while (textNode != null && labelSet.contains(textNode.elementType)) {
                textNode = textNode.treeNext
            }
            val reference = TwigVariableReference(element)
            return arrayOf(reference)
        }

        return PsiReference.EMPTY_ARRAY
    }
}
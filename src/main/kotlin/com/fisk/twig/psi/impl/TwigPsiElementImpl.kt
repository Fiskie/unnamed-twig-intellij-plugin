package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigPsiElement
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentationProviders
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import com.intellij.psi.util.PsiUtilCore
import java.util.*

open class TwigPsiElementImpl(astNode: ASTNode) : ASTWrapperPsiElement(astNode), TwigPsiElement {
    override fun getPresentation() = ItemPresentationProviders.getItemPresentation(this)

    override fun getReferences(): Array<out PsiReference> = ReferenceProvidersRegistry.getReferencesFromProviders(this)

    /**
     * Get all children for an element, including whitespace and content
     */
    override fun getAllChildren(): Array<PsiElement> {
        var psiChild: PsiElement? = firstChild ?: return PsiElement.EMPTY_ARRAY

        val result = ArrayList<PsiElement>()
        while (psiChild != null) {
            result.add(psiChild)
            psiChild = psiChild.nextSibling
        }
        return PsiUtilCore.toPsiElementArray(result)
    }
}
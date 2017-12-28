package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigPsiElement
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentationProviders
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry

class TwigPsiElementImpl(astNode: ASTNode) : ASTWrapperPsiElement(astNode), TwigPsiElement {
    override fun getPresentation() = ItemPresentationProviders.getItemPresentation(this)

    override fun getReferences(): Array<out PsiReference> = ReferenceProvidersRegistry.getReferencesFromProviders(this)
}
package com.fisk.twig.psi.reference

import com.fisk.twig.psi.impl.TwigLabelImpl
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar

class ReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(TwigLabelImpl::class.java), TwigLabelReferenceProvider())
    }
}

package com.fisk.twig.psi.reference

import com.fisk.twig.psi.impl.TwigLabelImpl
import com.fisk.twig.psi.impl.TwigTagImpl
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.util.ProcessingContext

class ReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(TwigLabelImpl::class.java), TwigLabelReferenceProvider())
    }
}
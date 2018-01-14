package com.fisk.twig.reference

import com.fisk.twig.psi.TwigMacroDeclaration
import com.fisk.twig.psi.impl.TwigBlockLabelImpl
import com.fisk.twig.psi.impl.TwigMacroDeclarationImpl
import com.fisk.twig.psi.impl.TwigVariableImpl
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiReferenceContributor
import com.intellij.psi.PsiReferenceRegistrar

class ReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(TwigVariableImpl::class.java), TwigVariableReferenceProvider())
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(TwigBlockLabelImpl::class.java), TwigBlockLabelReferenceProvider())
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(TwigMacroDeclarationImpl::class.java), TwigMacroDeclarationReferenceProvider())
    }
}

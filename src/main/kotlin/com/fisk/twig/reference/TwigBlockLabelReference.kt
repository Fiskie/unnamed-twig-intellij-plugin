package com.fisk.twig.reference

import com.fisk.twig.psi.TwigBlockLabel
import com.fisk.twig.psi.TwigPsiElement
import com.fisk.twig.psi.TwigVariable
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiReference

class TwigBlockLabelReference(val el: TwigPsiElement) : PsiReference {
    override fun getElement() = el

    override fun resolve() = el

    override fun getVariants() = emptyArray<Any>()

    override fun getRangeInElement() = TextRange(0, el.textLength)

    override fun getCanonicalText(): String = el.text

    override fun handleElementRename(newElementName: String?): PsiElement {
        if (el is PsiNamedElement) {
            el.setName(newElementName ?: "")
        }

        return el
    }

    override fun bindToElement(element: PsiElement) = el

    override fun isSoft() = false

    override fun isReferenceTo(other: PsiElement?): Boolean {
        return false
    }
}
package com.fisk.twig.psi

import com.intellij.psi.PsiElement

interface TwigPsiElement : PsiElement {
    fun getName(): String?
}
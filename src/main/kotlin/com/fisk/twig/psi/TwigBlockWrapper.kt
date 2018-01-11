package com.fisk.twig.psi

interface TwigBlockWrapper : TwigPsiElement {
    fun getStartStatement(): TwigBlockStartStatement?

    fun getContents(): TwigBlock?

    fun getEndStatement(): TwigBlockEndStatement?
}

package com.fisk.twig.psi

interface TwigBlockWrapper : TwigPsiElement {
    fun getStartStatement(): TwigStatement?

    fun getContents(): TwigBlock?

    fun getEndStatement(): TwigBlockEndStatement?
}

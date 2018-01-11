package com.fisk.twig.psi

interface TwigStatement : TwigPsiElement {
    fun getTag(): TwigTag?
}
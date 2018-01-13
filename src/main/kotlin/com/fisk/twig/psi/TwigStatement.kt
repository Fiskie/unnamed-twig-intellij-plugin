package com.fisk.twig.psi

interface TwigStatement : TwigPsiElement {
    val tag: TwigTag?
    fun getStatementContents(): String
}
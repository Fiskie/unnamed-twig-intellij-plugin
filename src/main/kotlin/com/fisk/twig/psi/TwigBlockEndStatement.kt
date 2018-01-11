package com.fisk.twig.psi

interface TwigBlockEndStatement : TwigStatement {
    fun getMatchingStartStatement(): TwigBlockStartStatement?
}
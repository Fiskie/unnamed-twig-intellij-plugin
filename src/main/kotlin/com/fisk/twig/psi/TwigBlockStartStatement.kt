package com.fisk.twig.psi

interface TwigBlockStartStatement : TwigStatement {
    fun getMatchingEndStatement(): TwigBlockEndStatement?
}

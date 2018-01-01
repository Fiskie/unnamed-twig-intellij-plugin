package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigString
import com.intellij.lang.ASTNode

class TwigStringImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigString {
    // The first and last characters will be single/double quotes
    override fun getName() = text.substring(1, text.length - 1)
}
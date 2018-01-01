package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigString
import com.intellij.lang.ASTNode

class TwigStringImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigString {
    override fun getName() = text
}
package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigProperty
import com.intellij.lang.ASTNode

class TwigPropertyImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigProperty {
    override fun getName(): String = text
}
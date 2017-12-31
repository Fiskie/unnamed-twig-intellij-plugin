package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigTag
import com.intellij.lang.ASTNode

class TwigTagImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigTag {
    override fun getName(): String? {
        return this.text
    }
}
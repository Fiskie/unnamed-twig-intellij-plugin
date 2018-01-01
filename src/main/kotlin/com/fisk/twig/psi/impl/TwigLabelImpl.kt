package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigLabel
import com.intellij.lang.ASTNode

class TwigLabelImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigLabel {
    override fun getName() = text
}
package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigBlock
import com.intellij.lang.ASTNode

class TwigBlockImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigBlock
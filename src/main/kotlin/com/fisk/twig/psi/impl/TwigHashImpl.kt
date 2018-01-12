package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigHash
import com.intellij.lang.ASTNode

class TwigHashImpl(node: ASTNode): TwigPsiElementImpl(node), TwigHash
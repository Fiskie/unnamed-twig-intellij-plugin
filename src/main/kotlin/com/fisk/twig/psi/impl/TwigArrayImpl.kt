package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigArray
import com.fisk.twig.psi.TwigHash
import com.intellij.lang.ASTNode

class TwigArrayImpl(node: ASTNode): TwigPsiElementImpl(node), TwigArray
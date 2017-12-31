package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigVariable
import com.intellij.lang.ASTNode

class TwigVariableImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigVariable
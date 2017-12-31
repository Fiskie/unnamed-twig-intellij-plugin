package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigStatementBlock
import com.intellij.lang.ASTNode

class TwigStatementBlockImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigStatementBlock
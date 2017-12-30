package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigStatement
import com.intellij.lang.ASTNode

class TwigStatementImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigStatement
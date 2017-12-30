package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigExpression
import com.intellij.lang.ASTNode

class TwigExpressionImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigExpression
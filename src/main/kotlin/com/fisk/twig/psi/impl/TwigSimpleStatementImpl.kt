package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigSimpleStatement
import com.intellij.lang.ASTNode

class TwigSimpleStatementImpl(node: ASTNode) : TwigStatementImpl(node), TwigSimpleStatement
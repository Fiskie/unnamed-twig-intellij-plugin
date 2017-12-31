package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigBlockStartStatement
import com.intellij.lang.ASTNode

class TwigBlockStartStatementImpl(node: ASTNode) : TwigStatementImpl(node), TwigBlockStartStatement

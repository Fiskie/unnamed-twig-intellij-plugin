package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigBlockEndStatement
import com.intellij.lang.ASTNode

class TwigBlockEndStatementImpl(node: ASTNode) : TwigStatementImpl(node), TwigBlockEndStatement

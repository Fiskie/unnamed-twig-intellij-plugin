package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigBlockWrapper
import com.intellij.lang.ASTNode

class TwigBlockWrapperImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigBlockWrapper
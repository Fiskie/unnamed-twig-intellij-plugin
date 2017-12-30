package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigComment
import com.intellij.lang.ASTNode

class TwigCommentImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigComment
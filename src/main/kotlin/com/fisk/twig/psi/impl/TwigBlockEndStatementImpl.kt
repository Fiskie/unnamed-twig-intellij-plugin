package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigBlockEndStatement
import com.fisk.twig.psi.TwigBlockStartStatement
import com.intellij.lang.ASTNode

class TwigBlockEndStatementImpl(node: ASTNode) : TwigStatementImpl(node), TwigBlockEndStatement {
    override fun getMatchingStartStatement(): TwigBlockStartStatement? {
        return parent.firstChild as? TwigBlockStartStatement
    }
}

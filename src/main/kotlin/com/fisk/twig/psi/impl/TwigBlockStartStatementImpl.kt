package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigBlockEndStatement
import com.fisk.twig.psi.TwigBlockStartStatement
import com.intellij.lang.ASTNode

class TwigBlockStartStatementImpl(node: ASTNode) : TwigStatementImpl(node), TwigBlockStartStatement {
    override fun getMatchingEndStatement(): TwigBlockEndStatement? {
        return parent.lastChild as? TwigBlockEndStatement
    }
}
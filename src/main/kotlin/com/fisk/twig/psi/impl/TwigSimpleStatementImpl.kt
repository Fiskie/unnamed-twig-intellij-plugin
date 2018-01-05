package com.fisk.twig.psi.impl

import com.fisk.twig.config.TwigIcons
import com.fisk.twig.psi.TwigSimpleStatement
import com.intellij.lang.ASTNode
import javax.swing.Icon

class TwigSimpleStatementImpl(node: ASTNode) : TwigStatementImpl(node), TwigSimpleStatement {
    override fun getIcon(flags: Int): Icon {
        return TwigIcons.Elements.statement_brace
    }
}
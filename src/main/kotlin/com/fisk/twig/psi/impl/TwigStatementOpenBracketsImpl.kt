package com.fisk.twig.psi.impl

import com.fisk.twig.config.TwigIcons
import com.fisk.twig.psi.TwigStatementOpenBrackets
import com.intellij.lang.ASTNode
import javax.swing.Icon

class TwigStatementOpenBracketsImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigStatementOpenBrackets {
    override fun getIcon(flags: Int): Icon {
        return TwigIcons.Elements.statement_brace
    }
}
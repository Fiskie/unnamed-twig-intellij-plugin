package com.fisk.twig.psi.impl

import com.fisk.twig.config.TwigIcons
import com.fisk.twig.psi.TwigExpression
import com.intellij.lang.ASTNode
import javax.swing.Icon

class TwigExpressionImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigExpression {
    override fun getIcon(flags: Int): Icon {
        return TwigIcons.Elements.expression_brace
    }
}
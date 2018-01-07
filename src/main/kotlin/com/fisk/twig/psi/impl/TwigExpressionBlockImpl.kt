package com.fisk.twig.psi.impl

import com.fisk.twig.config.TwigIcons
import com.fisk.twig.psi.TwigExpressionBlock
import com.intellij.lang.ASTNode
import javax.swing.Icon

class TwigExpressionBlockImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigExpressionBlock {
    override fun getName(): String? {
        if (text.length > 6) {
            // todo: will screw up if there's no padding (e.g. {{foo}}
            // do this nicely by looking between the expr open/close token positions
            return text.substring(3, text.length - 3).trim()
        }

        return "(invalid expression)"
    }

    override fun getIcon(flags: Int): Icon {
        return TwigIcons.Elements.expression_brace
    }
}
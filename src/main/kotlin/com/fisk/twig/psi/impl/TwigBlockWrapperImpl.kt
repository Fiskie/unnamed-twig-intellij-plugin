package com.fisk.twig.psi.impl

import com.fisk.twig.config.TwigIcons
import com.fisk.twig.psi.*
import com.intellij.lang.ASTNode
import javax.swing.Icon

class TwigBlockWrapperImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigBlockWrapper {
    override fun getName(): String? {
        return getStartStatement()?.getStatementContents()
    }

    override fun getIcon(flags: Int): Icon {
        return TwigIcons.Elements.statement_brace
    }

    override fun getStartStatement(): TwigStatement? {
        return firstChild as? TwigBlockStartStatement ?: firstChild as? TwigSimpleStatement
    }

    override fun getContents(): TwigBlock? {
        return if (children.size > 1) {
            children[1] as? TwigBlock
        } else {
            null
        }
    }

    override fun getEndStatement(): TwigBlockEndStatement? {
        return lastChild as? TwigBlockEndStatement
    }
}
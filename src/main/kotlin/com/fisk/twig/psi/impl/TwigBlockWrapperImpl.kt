package com.fisk.twig.psi.impl

import com.fisk.twig.config.TwigIcons
import com.fisk.twig.psi.*
import com.intellij.lang.ASTNode
import javax.swing.Icon

class TwigBlockWrapperImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigBlockWrapper {
    override fun getName(): String? {
        val openTag = getStartStatement()

        openTag?.let {
            // todo: ugly, but will work (?) (not with non-spaced tags it won't)
            return openTag.text.substring(3, openTag.text.length - 3).trim()
        }

        return null
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
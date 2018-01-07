package com.fisk.twig.psi.impl

import com.fisk.twig.config.TwigIcons
import com.fisk.twig.psi.*
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import javax.swing.Icon

class TwigBlockWrapperImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigBlockWrapper {
    override fun getName(): String? {
        val openTag = getBlockOpenStatement()

        openTag?.let {
            // todo: ugly, but will work (?) (not with non-spaced tags it won't)
            return openTag.text.substring(3, openTag.text.length - 3).trim()
        }

        return null
    }

    fun getBlockOpenStatement(): TwigStatement? {
        return PsiTreeUtil.findChildOfType(this, TwigBlockStartStatement::class.java) ?:
                PsiTreeUtil.findChildOfType(this, TwigSimpleStatement::class.java) ?:
                PsiTreeUtil.findChildOfType(this, TwigInverseStatement::class.java)
    }

    override fun getIcon(flags: Int): Icon {
        return TwigIcons.Elements.statement_brace
    }
}
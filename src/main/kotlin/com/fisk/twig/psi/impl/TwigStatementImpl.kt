package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigStatement
import com.fisk.twig.psi.TwigTag
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

abstract class TwigStatementImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigStatement {
    override fun getName(): String? {
        return getTag()?.name
    }

    override fun getTag(): TwigTag? {
        return PsiTreeUtil.findChildOfType(this, TwigTag::class.java)
    }
}
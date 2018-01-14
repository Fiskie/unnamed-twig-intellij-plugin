package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigStatement
import com.fisk.twig.psi.TwigTag
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

abstract class TwigStatementImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigStatement {
    override fun getName(): String? {
        return tag?.name
    }

    override fun getStatementContents(): String {
        val start = node.firstChildNode.textLength
        val end = node.textLength - node.lastChildNode.textLength
        return text.substring(start, end).trim()
    }

    override val tag: TwigTag?
        get() = PsiTreeUtil.findChildOfType(this, TwigTag::class.java)
}
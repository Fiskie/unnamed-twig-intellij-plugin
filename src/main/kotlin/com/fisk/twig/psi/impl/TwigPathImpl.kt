package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigPath
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.PsiTreeUtil

class TwigPathImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigPath {
    override fun getName(): String = text

    override fun setName(name: String): PsiElement {
        val current = PsiTreeUtil.getChildOfType(this, LeafPsiElement::class.java)
        // todo
//        node.replaceChild(content, )
        return this
    }
}
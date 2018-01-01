package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigLabel
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.PsiTreeUtil

class TwigLabelImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigLabel {
    override fun getName(): String = text

    override fun setName(name: String) : PsiElement {
        val current = PsiTreeUtil.getChildOfType(this, LeafPsiElement::class.java)
        // todo
//        node.replaceChild(content, )
        return this
    }
}
package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigPsiElement
import com.fisk.twig.psi.util.TwigPsiUtil
import com.fisk.twig.psi.TwigTag
import com.fisk.twig.psi.util.TwigElementFactory
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement


class TwigTagImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigTag {
    override fun getName(): String? {
        return text
    }

    override fun setName(newName: String): PsiElement {
        val keyNode = firstChild.node
        if (keyNode != null) {
            val property = TwigElementFactory.createProperty(project, newName)
            val newKeyNode = property.firstChild.node
            node.replaceChild(keyNode, newKeyNode)
        }
        return this
    }

    override fun getNameIdentifier(): PsiElement? {
        return firstChild.node?.psi
    }
}
package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigPath
import com.fisk.twig.psi.util.TwigElementFactory
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

class TwigPathImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigPath {
    override fun getName(): String? {
        return text
    }

    override fun setName(newName: String): PsiElement {
        val keyNode = firstChild.node
        if (keyNode != null) {
            val property = TwigElementFactory.createVariable(project, newName)
            val newKeyNode = property.firstChild.node
            node.replaceChild(keyNode, newKeyNode)
        }
        return this
    }

    override fun getNameIdentifier(): PsiElement? {
        return firstChild.node?.psi
    }
}
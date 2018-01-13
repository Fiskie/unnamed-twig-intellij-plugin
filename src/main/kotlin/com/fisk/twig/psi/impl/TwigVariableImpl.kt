package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigVariable
import com.fisk.twig.reference.TwigVariableReference
import com.fisk.twig.psi.util.TwigElementFactory
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

class TwigVariableImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigVariable {
    override fun getName(): String = text

    override fun setName(newName: String): PsiElement {
        val keyNode = firstChild.node
        if (keyNode != null) {
            val property = TwigElementFactory.createVariable(project, newName)
            val newKeyNode = property.firstChild.node
            node.replaceChild(keyNode, newKeyNode)
        }
        return this
    }

    override fun getNameIdentifier() = firstChild.node?.psi

    override fun getReference() = TwigVariableReference(this)
}
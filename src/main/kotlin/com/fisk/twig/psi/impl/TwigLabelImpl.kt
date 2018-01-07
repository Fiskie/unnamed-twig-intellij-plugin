package com.fisk.twig.psi.impl

import com.fisk.twig.psi.TwigLabel
import com.fisk.twig.psi.reference.TwigLabelReference
import com.fisk.twig.psi.util.TwigElementFactory
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

class TwigLabelImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigLabel {
    override fun getName(): String = text

    override fun setName(newName: String): PsiElement {
        val keyNode = firstChild.node
        if (keyNode != null) {
            val property = TwigElementFactory.createProperty(project, newName)
            val newKeyNode = property.firstChild.node
            node.replaceChild(keyNode, newKeyNode)
        }
        return this
    }

    override fun getNameIdentifier() = firstChild.node?.psi

    override fun getReference() = TwigLabelReference(this)
}
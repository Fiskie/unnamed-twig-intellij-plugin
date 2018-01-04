package com.fisk.twig.psi.impl

import com.fisk.twig.psi.*
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class TwigBlockWrapperImpl(node: ASTNode) : TwigPsiElementImpl(node), TwigBlockWrapper {
    override fun getName(): String? {
        val openTag = getBlockOpenStatement()

        openTag?.let {
            // todo: ugly, but will work (?)
            return openTag.text.substring(3, openTag.text.length - 3).trim()
        }

        return null
    }

    fun getBlockOpenStatement() : TwigStatement? {
        return PsiTreeUtil.findChildOfType(this, TwigBlockStartStatement::class.java) ?:
                PsiTreeUtil.findChildOfType(this, TwigSimpleStatement::class.java) ?:
                PsiTreeUtil.findChildOfType(this, TwigInverseStatement::class.java)
    }
}
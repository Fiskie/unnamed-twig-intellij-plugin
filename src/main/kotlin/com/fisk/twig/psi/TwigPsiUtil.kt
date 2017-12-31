package com.fisk.twig.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

object TwigPsiUtil {
    fun findParentOpenTagElement(element: PsiElement): TwigStatementOpenBrackets {
        return PsiTreeUtil.findFirstParent(element, true) { element1 -> element1 != null && element1 is TwigStatementOpenBrackets } as TwigStatementOpenBrackets
    }

    fun findParentCloseTagElement(element: PsiElement): TwigStatementCloseBrackets {
        return PsiTreeUtil.findFirstParent(element, true) { element1 -> element1 != null && element1 is TwigStatementCloseBrackets } as TwigStatementCloseBrackets
    }

    /**
     * Tests to see if the given element is not the "root" statements expression of the grammar
     */
    fun isNonRootBlockElement(element: PsiElement): Boolean {
        val statementsParent = PsiTreeUtil.findFirstParent(element, true) { element1 -> element1 != null && element1 is TwigBlock }

        // we're a non-root statements if we're of type statements, and we have a statements parent
        return element is TwigBlock && statementsParent != null
    }
}
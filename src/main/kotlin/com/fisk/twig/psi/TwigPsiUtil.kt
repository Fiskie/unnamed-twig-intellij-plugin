package com.fisk.twig.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class TwigPsiUtil {
    /**
     * Used to determine if an element is part of an "open tag" (i.e. "{{#open}}" or "{{^openInverse}}")
     *
     *
     * If the given element is the descendant of an [HbOpenBlockMustache], this method returns
     * that parent.
     *
     *
     * Otherwise, returns null.
     *
     * @param element The element whose ancestors will be searched
     * @return An ancestor of type [HbOpenBlockMustache] or null if none exists
     */
    fun findParentOpenTagElement(element: PsiElement): TwigStatementOpen {
        return PsiTreeUtil.findFirstParent(element, true) { element1 -> element1 != null && element1 is TwigStatementOpen } as TwigStatementOpen
    }

    /**
     * Used to determine if an element is part of a "close tag" (i.e. "{{/closer}}")
     *
     *
     * If the given element is the descendant of an [HbCloseBlockMustache], this method returns that parent.
     *
     *
     * Otherwise, returns null.
     *
     *
     *
     * @param element The element whose ancestors will be searched
     * @return An ancestor of type [HbCloseBlockMustache] or null if none exists
     */
    fun findParentCloseTagElement(element: PsiElement): TwigStatementClose {
        return PsiTreeUtil.findFirstParent(element, true) { element1 -> element1 != null && element1 is TwigStatementClose } as TwigStatementClose
    }
}
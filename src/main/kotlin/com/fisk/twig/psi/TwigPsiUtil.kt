package com.fisk.twig.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import kotlin.math.exp

object TwigPsiUtil {
    val DEFAULT_BLOCK_TAGS = setOf("if", "for", "block", "embed", "spaceless")
    val INVERSE_TAGS = setOf("else", "elseif")
    val INVERSE_ALLOWED = setOf("if", "for")

    fun findParentOpenTagElement(element: PsiElement?): TwigStatementOpenBrackets? {
        val el = PsiTreeUtil.findFirstParent(element, true) { element1 -> element1 != null && element1 is TwigStatementOpenBrackets }

        el?.let {
            return el as TwigStatementOpenBrackets
        }

        return null
    }

    fun findParentCloseTagElement(element: PsiElement?): TwigStatementCloseBrackets? {
        val el = PsiTreeUtil.findFirstParent(element, true) { element1 -> element1 != null && element1 is TwigStatementCloseBrackets }

        el?.let {
            return el as TwigStatementCloseBrackets
        }

        return null
    }

    /**
     * Tests to see if the given element is not the "root" statements expression of the grammar
     */
    fun isNonRootBlockElement(element: PsiElement): Boolean {
        val statementsParent = PsiTreeUtil.findFirstParent(element, true) { element1 -> element1 != null && element1 is TwigBlock }

        // we're a non-root statements if we're of type statements, and we have a statements parent
        return element is TwigBlock && statementsParent != null
    }

    fun isDefaultBlockTag(tag: String): Boolean {
        return DEFAULT_BLOCK_TAGS.contains(tag)
    }

    fun isInverseTag(tag: String): Boolean {
        return INVERSE_TAGS.contains(tag)
    }

    /**
     * Returns true if an inverse statement can be used for this tag
     */
    fun allowsInverseTag(tag: String): Boolean {
        return INVERSE_ALLOWED.contains(tag)
    }
}
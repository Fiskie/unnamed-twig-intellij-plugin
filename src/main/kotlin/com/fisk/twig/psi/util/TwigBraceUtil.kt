package com.fisk.twig.psi.util

import com.fisk.twig.parsing.TwigTokenTypes
import com.intellij.psi.tree.IElementType

object TwigBraceUtil {
    fun getCloseBraceForElement(elementType: IElementType) = when (elementType) {
        TwigTokenTypes.STATEMENT_OPEN -> TwigTokenTypes.STATEMENT_CLOSE
        TwigTokenTypes.EXPRESSION_OPEN -> TwigTokenTypes.EXPRESSION_CLOSE
        TwigTokenTypes.COMMENT_OPEN -> TwigTokenTypes.COMMENT_CLOSE
        else -> null
    }

    fun getOpenBraceForElement(elementType: IElementType) = when (elementType) {
        TwigTokenTypes.STATEMENT_CLOSE -> TwigTokenTypes.STATEMENT_OPEN
        TwigTokenTypes.EXPRESSION_CLOSE -> TwigTokenTypes.EXPRESSION_OPEN
        TwigTokenTypes.COMMENT_CLOSE -> TwigTokenTypes.COMMENT_OPEN
        else -> null
    }
}
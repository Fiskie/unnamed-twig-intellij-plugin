package com.fisk.twig

import com.intellij.lang.HtmlScriptContentProvider
import com.intellij.lexer.Lexer
import com.intellij.psi.tree.IElementType

class TwigScriptContentProvider : HtmlScriptContentProvider {
    override fun getScriptElementType() = null
    override fun getHighlightingLexer() = null
}
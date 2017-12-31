package com.fisk.twig

import com.intellij.lang.HtmlScriptContentProvider

class TwigScriptContentProvider : HtmlScriptContentProvider {
    override fun getScriptElementType() = null
    override fun getHighlightingLexer() = null
}
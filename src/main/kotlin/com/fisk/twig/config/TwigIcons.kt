package com.fisk.twig.config

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object TwigIcons {
    private fun load(path: String) = IconLoader.getIcon(path, TwigIcons::class.java)

    val file_icon = load("/icons/file.png")

    object Elements {
        val statement_brace = load("/icons/elements/statementBrace.png")
        val expression_brace = load("/icons/elements/expressionBrace.png")
    }
}
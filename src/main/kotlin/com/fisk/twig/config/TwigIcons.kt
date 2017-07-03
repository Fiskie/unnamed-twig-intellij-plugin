package com.fisk.twig.config

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object TwigIcons {
    private fun load(path: String): Icon {
        return IconLoader.getIcon(path, TwigIcons::class.java)
    }

    val file_icon = load("/icons/file.png") // 16x16
}
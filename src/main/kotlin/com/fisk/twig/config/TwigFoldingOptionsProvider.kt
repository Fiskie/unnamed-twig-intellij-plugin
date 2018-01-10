package com.fisk.twig.config

import com.fisk.twig.TwigBundle
import com.intellij.application.options.editor.CodeFoldingOptionsProvider
import com.intellij.openapi.options.BeanConfigurable

class TwigFoldingOptionsProvider : BeanConfigurable<TwigFoldingOptionsProvider.TwigCodeFoldingOptionsBean>(TwigCodeFoldingOptionsBean), CodeFoldingOptionsProvider {
    object TwigCodeFoldingOptionsBean {
        fun isAutoCollapseBlocks() = TwigConfig.isAutoCollapseBlocksEnabled

        fun setAutoCollapseBlocks(value: Boolean) {
            TwigConfig.setAutoCollapseBlocks(value)
        }
    }

    init {
        val getter: () -> Boolean = { instance.isAutoCollapseBlocks() }
        val setter: (Boolean) -> Unit = { v -> instance.setAutoCollapseBlocks(v) }

        checkBox(TwigBundle.message("twig.pages.folding.auto.collapse.blocks"), getter, setter)
    }
}
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
//        checkBox(TwigBundle.message("twig.pages.folding.auto.collapse.blocks"), instance.isAutoCollapseBlocks())
    }
}
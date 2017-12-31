package com.fisk.twig.editor.templates

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider

class TwigLiveTemplatesProvider : DefaultLiveTemplatesProvider {
    override fun getDefaultLiveTemplateFiles() = arrayOf("liveTemplates/Twig")

    override fun getHiddenLiveTemplateFiles() = null
}

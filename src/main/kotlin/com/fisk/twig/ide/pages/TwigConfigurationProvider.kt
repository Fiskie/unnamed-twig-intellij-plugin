package com.fisk.twig.ide.pages

import com.intellij.openapi.options.ConfigurableProvider
import com.intellij.openapi.project.Project

class TwigConfigurationProvider(val project: Project) : ConfigurableProvider() {
    override fun createConfigurable() = ConfigurationPage(project)
}
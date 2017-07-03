package com.fisk.twig.pages

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurableProvider
import com.intellij.openapi.project.Project

class TwigConfigurationProvider(val project: Project) : ConfigurableProvider() {
    override fun createConfigurable(): Configurable? {
        return ConfigurationPage(project)
    }
}
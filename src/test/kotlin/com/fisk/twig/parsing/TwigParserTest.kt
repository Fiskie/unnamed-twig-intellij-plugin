package com.fisk.twig.parsing

import com.fisk.twig.TwigLanguage
import com.fisk.twig.util.TwigTestUtils
import com.intellij.ide.util.PropertiesComponent
import com.intellij.ide.util.PropertiesComponentImpl
import com.intellij.lang.LanguageParserDefinitions
import com.intellij.lang.ParserDefinition
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings
import com.intellij.psi.templateLanguages.TemplateDataLanguagePatterns
import com.intellij.testFramework.ParsingTestCase
import com.intellij.testFramework.PlatformLiteFixture

abstract class TwigParserTest : ParsingTestCase("parsing", "twig", TwigParserDefinition()) {
    override fun getTestDataPath() = TwigTestUtils.BASE_TEST_DATA_PATH
    override fun checkAllPsiRoots() = false

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()

        val appContainer = PlatformLiteFixture.getApplication().getPicoContainer()
        appContainer.registerComponentInstance(PropertiesComponent::class.java.name,
                PropertiesComponentImpl.create())
        appContainer.registerComponentInstance(TemplateDataLanguageMappings::class.java.name,
                TemplateDataLanguageMappings(project))
        appContainer.registerComponentInstance(TemplateDataLanguagePatterns::class.java.name,
                TemplateDataLanguagePatterns())
        addExplicitExtension<ParserDefinition>(LanguageParserDefinitions.INSTANCE, TwigLanguage.INSTANCE, TwigParserDefinition())
    }
}
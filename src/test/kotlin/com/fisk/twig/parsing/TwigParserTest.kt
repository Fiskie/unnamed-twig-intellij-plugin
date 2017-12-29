package com.fisk.twig.parsing

import com.fisk.twig.util.TwigTestUtils
import com.intellij.testFramework.ParsingTestCase

abstract class TwigParserTest : ParsingTestCase("parser", "twig", TwigParserDefinition()) {
    override fun getTestDataPath() = TwigTestUtils.BASE_TEST_DATA_PATH
    override fun checkAllPsiRoots() = false
}
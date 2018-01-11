package com.fisk.twig

import com.fisk.twig.util.TwigTestUtils
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

abstract class TwigFixtureTestBase : LightPlatformCodeInsightFixtureTestCase() {
    protected val fileName: String
        get() = getTestName(true) + ".twig"

    override fun getTestDataPath(): String {
        return TwigTestUtils.BASE_TEST_DATA_PATH
    }
}
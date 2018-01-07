package com.fisk.twig.psi

import com.fisk.twig.util.TwigTestUtils
import com.intellij.testFramework.LightPlatformCodeInsightTestCase
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase

class TwigReferenceTest : LightPlatformCodeInsightFixtureTestCase() {
    override fun getTestDataPath() = TwigTestUtils.BASE_TEST_DATA_PATH
}
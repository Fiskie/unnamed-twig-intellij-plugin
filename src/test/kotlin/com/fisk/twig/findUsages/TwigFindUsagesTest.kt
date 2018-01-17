package com.fisk.twig.findUsages

import com.fisk.twig.TwigFixtureTestBase
import com.fisk.twig.util.TwigTestUtils
import junit.framework.Assert

class TwigFindUsagesTest : TwigFixtureTestBase() {
    override fun getTestDataPath() = TwigTestUtils.BASE_TEST_DATA_PATH + "/findUsages/"

    fun testVariable() {
        val usageInfos = myFixture.testFindUsages("Variable.twig")
        Assert.assertEquals(4, usageInfos.size)
    }
}
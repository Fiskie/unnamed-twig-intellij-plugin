package com.fisk.twig.rename

import com.fisk.twig.TwigFixtureTestBase
import com.fisk.twig.util.TwigTestUtils

class TwigRenameTest : TwigFixtureTestBase() {
    override fun getTestDataPath() = TwigTestUtils.BASE_TEST_DATA_PATH + "/rename/"

    fun testVariable() {
        myFixture.configureByFiles("Variable.twig")
        myFixture.renameElementAtCaret("baz")
        myFixture.configureByFiles("Variable.after.twig")
    }
}
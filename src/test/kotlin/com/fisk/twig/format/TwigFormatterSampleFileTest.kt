package com.fisk.twig.format

import com.intellij.lang.javascript.JavaScriptFileType

class TwigFormatterSampleFileTest : TwigFormatterTest() {
    // todo: restore this test
//    @Throws(Exception::class)
//    fun testMixedContent() {
//        doFileBasedTest()
//    }

    @Throws(Exception::class)
    fun testHTMLContent() {
        doFileBasedTest()
    }

    /**
     * Test out formatting with a non-HTML template data language
     *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun testSampleFileWithCustomTemplateDataLang() {
        doFileBasedTest("JSSampleFile.twig", JavaScriptFileType.INSTANCE)
    }
}

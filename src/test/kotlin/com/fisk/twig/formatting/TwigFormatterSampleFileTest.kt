package com.fisk.twig.formatting

import com.intellij.lang.javascript.JavaScriptFileType

class TwigFormatterSampleFileTest : TwigFormatterTest() {
    @Throws(Exception::class)
    fun testJSScriptTagIndent() {
        doFileBasedTest()
    }

    @Throws(Exception::class)
    fun testHTMLContent() {
        doFileBasedTest()
    }

    @Throws(Exception::class)
    fun testHTMLContinuationIndent() {
        doFileBasedTest()
    }

//    @Throws(Exception::class)
//    fun testTwigExpressionHashAlignment() {
//        doFileBasedTest()
//    }

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

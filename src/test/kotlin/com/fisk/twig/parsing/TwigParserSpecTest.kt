package com.fisk.twig.parsing

class TwigParserSpecTest : TwigParserTest() {
    fun testSimpleExpression() { doTest(true) }
    fun testComment() { doTest(true) }
    fun testUnclosedComment() { doTest(true) }
}
package com.fisk.twig.parsing

class TwigParserSpecTest : TwigParserTest() {
    fun testSimpleExpression() { doTest(true) }
    fun testComment() { doTest(true) }
    fun testUnclosedComment() { doTest(true) }
    fun testIfBlock() { doTest(true) }
    fun testEmptyIfBlock() { doTest(true) }
    fun testNestedIfBlock() { doTest(true) }
    fun testIfElseBlock() { doTest(true) }
    fun testSimpleSetStatement() { doTest(true) }
}
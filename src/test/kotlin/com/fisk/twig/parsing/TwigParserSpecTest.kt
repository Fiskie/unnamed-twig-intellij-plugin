package com.fisk.twig.parsing

class TwigParserSpecTest : TwigParserTest() {
    fun testSimpleExpression() {
        doTest(true)
    }

    fun testComment() {
        doTest(true)
    }

    fun testUnclosedComment() {
        doTest(true)
    }

    fun testIfBlock() {
        doTest(true)
    }

    fun testEmptyIfBlock() {
        doTest(true)
    }

    fun testNestedIfBlock() {
        doTest(true)
    }

    fun testSimpleIfElse() {
        doTest(true)
    }

    fun testIfElseChain() {
        doTest(true)
    }

    fun testSimpleSetStatement() {
        doTest(true)
    }

    fun testSimpleSetBlock() {
        // This test will help make sure we are properly implementing lookahead on end tags
        doTest(true)
    }
}
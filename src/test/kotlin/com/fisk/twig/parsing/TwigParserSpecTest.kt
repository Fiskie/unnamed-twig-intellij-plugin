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

    fun testPrematureEndTag() {
        doTest(true)
    }

    // TODO: fix test
//    fun testNestedPrematureEndTag() {
//        doTest(true)
//    }

    fun testSimpleSetBlock() {
        // This test will help make sure we are properly implementing lookahead on end tags
        doTest(true)
    }

    fun testNestedSetStatement() {
        // Ditto, check standalone statements aren't consuming the end tag of the current block
        doTest(true)
    }
}
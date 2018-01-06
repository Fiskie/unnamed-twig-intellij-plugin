package com.fisk.twig.parsing

class TwigParserFreeFormTest : TwigParserTest() {
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

    fun testNestedPrematureEndTag() {
        doTest(true)
    }

    fun testSimpleSetBlock() {
        // This test will help make sure we are properly implementing lookahead on end tags
        doTest(true)
    }

    fun testNestedSetStatement() {
        // Ditto, check standalone statements aren't consuming the end tag of the current block
        doTest(true)
    }

    fun testFilterChains() {
        doTest(true)
    }

    fun testStringOperations() {
        doTest(true)
    }

    fun testUnclosedIfElse() {
        doTest(true)
    }

    fun testUglyIfElseChain() {
        doTest(true)
    }

    fun testUnformattedSetStatement() {
        doTest(true)
    }

    fun testEmptyStatement() {
        doTest(true)
    }

    fun testNestedEmptyStatement() {
        // todo: write expected response
        // this is going to fail because inverse checks are consuming empty {% %} tags
        doTest(true)
    }
}
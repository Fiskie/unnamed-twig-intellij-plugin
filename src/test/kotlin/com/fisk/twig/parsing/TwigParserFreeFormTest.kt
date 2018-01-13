package com.fisk.twig.parsing

class TwigParserFreeFormTest : TwigParserTest() {
    fun testComment() {
        doTest(true)
    }

    fun testUnclosedComment() {
        doTest(true)
    }

    fun testIfBlock() {
        doTest(true)
    }

    fun testEmptyBlock() {
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

    fun testHtmlTodo() {
        doTest(true)
    }

    fun testJSContent() {
        doTest(true)
    }

    fun testLooseBraces() {
        // Make sure CONTENT is being parsed properly
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

    fun testExpressions() {
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

    fun testDoubleRBraceInObject() {
        // makes sure }} in an object isn't being parsed as an expression closer
        doTest(true)
    }

    fun testNestedEmptyStatement() {
        doTest(true)
    }

    fun testMultipleSets() {
        doTest(true)
    }

    fun testCustomTag() {
        doTest(true)
    }

    fun testNamedArguments() {
        doTest(true)
    }

    fun testFilteredForLoop() {
        doTest(true)
    }

    fun testFilterHash() {
        doTest(true)
    }

    fun testSubexpressions() {
        doTest(true)
    }

    fun testInterpolatedString() {
        doTest(true)
    }

    fun testTestOperator() {
        doTest(true)
    }

    fun testMacroDeclaration() {
        doTest(true)
    }
}
package com.fisk.twig.parsing

import com.intellij.psi.tree.TokenSet

object TwigTokenTypes {
    @JvmField
    val CONTENT = TwigElementType("CONTENT", "twig.parsing.element.expected.content")
    @JvmField
    val BOOLEAN = TwigElementType("BOOLEAN", "twig.parsing.element.expected.boolean")
    @JvmField
    val WHITE_SPACE = TwigElementType("WHITE_SPACE", "twig.parsing.element.expected.white_space")
    @JvmField
    val COMMENT_OPEN = TwigElementType("COMMENT_OPEN", "twig.parsing.element.expected.comment_open")
    @JvmField
    val COMMENT_CLOSE = TwigElementType("COMMENT_CLOSE", "twig.parsing.element.expected.comment_close")
    @JvmField
    val UNCLOSED_COMMENT = TwigElementType("UNCLOSED_COMMENT", "twig.parsing.element.expected.comment_close")
    @JvmField
    val COMMENT_CONTENT = TwigElementType("COMMENT_CONTENT", "twig.parsing.element.expected.comment_content")
    @JvmField
    val STATEMENT_OPEN = TwigElementType("STATEMENT_OPEN", "twig.parsing.element.expected.statement_open")
    @JvmField
    val STATEMENT_CLOSE = TwigElementType("STATEMENT_CLOSE", "twig.parsing.element.expected.statement_close")
    @JvmField
    val EXPRESSION_OPEN = TwigElementType("EXPRESSION_OPEN", "twig.parsing.element.expected.expression_open")
    @JvmField
    val EXPRESSION_CLOSE = TwigElementType("EXPRESSION_CLOSE", "twig.parsing.element.expected.expression_close")
    @JvmField
    val OPEN_SEXPR = TwigElementType("OPEN_SEXPR", "twig.parsing.element.expected.open_sexpr")
    @JvmField
    val CLOSE_SEXPR = TwigElementType("CLOSE_SEXPR", "twig.parsing.element.expected.close_sexpr")
    @JvmField
    val OPEN_LIST = TwigElementType("OPEN_LIST", "twig.parsing.element.expected.open_list")
    @JvmField
    val CLOSE_LIST = TwigElementType("CLOSE_LIST", "twig.parsing.element.expected.close_list")
    @JvmField
    val OPEN_DICT = TwigElementType("OPEN_DICT", "twig.parsing.element.expected.open_list")
    @JvmField
    val CLOSE_DICT = TwigElementType("CLOSE_DICT", "twig.parsing.element.expected.close_dict")
    @JvmField
    val EQUALS = TwigElementType("EQUALS", "twig.parsing.element.expected.equals")
    @JvmField
    val SEP = TwigElementType("SEP", "twig.parsing.element.expected.sep")
    @JvmField
    val OPERATOR = TwigElementType("ELSE", "twig.parsing.element.expected.operator")
    @JvmField
    val NUMBER = TwigElementType("NUMBER", "twig.parsing.element.expected.integer")
    @JvmField
    val STRING = TwigElementType("STRING", "twig.parsing.element.expected.string")
    @JvmField
    val VARIABLE = TwigElementType("VARIABLE", "twig.parsing.element.expected.variable")
    @JvmField
    val TAG = TwigElementType("TAG", "twig.parsing.element.expected.tag")
    @JvmField
    val INVALID = TwigElementType("INVALID", "twig.parsing.element.expected.invalid")
    @JvmField
    val FILTER_SEP = TwigElementType("FILTER_SEP", "twig.parsing.element.expected.filter_sep")
    @JvmField
    val TEST = TwigElementType("TEST", "twig.parsing.element.expected.test")

    @JvmField
    val STATEMENT = TwigCompositeElementType("STATEMENT")

    @JvmField
    val EXPRESSION = TwigCompositeElementType("EXPRESSION")

    val STRING_LITERALS = TokenSet.create(STRING)
    val COMMENTS = TokenSet.create(COMMENT_CONTENT, COMMENT_CLOSE, COMMENT_OPEN)
}
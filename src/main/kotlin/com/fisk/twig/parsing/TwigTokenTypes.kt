package com.fisk.twig.parsing

import com.intellij.psi.tree.TokenSet

object TwigTokenTypes {
    @JvmField
    val CONTENT = TwigElementType("CONTENT", "twig.parsing.element.expected.content")
    @JvmField
    val OUTER_ELEMENT_TYPE = TwigElementType("TWIG_FRAGMENT", "twig.parsing.element.expected.outer_element_type")
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
    val LPARENTH = TwigElementType("LPARENTH", "twig.parsing.element.expected.open_sexpr")
    @JvmField
    val RPARENTH = TwigElementType("RPARENTH", "twig.parsing.element.expected.close_sexpr")
    @JvmField
    val LBRACKET = TwigElementType("LBRACKET", "twig.parsing.element.expected.open_list")
    @JvmField
    val RBRACKET = TwigElementType("RBRACKET", "twig.parsing.element.expected.close_list")
    @JvmField
    val LBRACE = TwigElementType("LBRACE", "twig.parsing.element.expected.open_hash")
    @JvmField
    val RBRACE = TwigElementType("RBRACE", "twig.parsing.element.expected.close_hash")
    @JvmField
    val EQUALS = TwigElementType("EQUALS", "twig.parsing.element.expected.equals")
    @JvmField
    val SEP = TwigElementType("SEP", "twig.parsing.element.expected.sep")
    @JvmField
    val OPERATOR = TwigElementType("OPERATOR", "twig.parsing.element.expected.operator")
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
    val PROPERTY = TwigElementType("PROPERTY", "twig.parsing.element.expected.property")
    @JvmField
    val COMMA = TwigElementType("COMMA", "twig.parsing.element.expected.comma")

    @JvmField
    val STATEMENT = TwigCompositeElementType("STATEMENT")

    @JvmField
    val EXPRESSION = TwigCompositeElementType("EXPRESSION")

    @JvmField
    val COMMENT = TwigElementType("COMMENT", "twig.parsing.element.expected.comment")

    @JvmField
    val BLOCK_WRAPPER = TwigCompositeElementType("BLOCK_WRAPPER")

    @JvmField
    val BLOCK_START_STATEMENT = TwigCompositeElementType("BLOCK_START_STATEMENT")

    @JvmField
    val INVERSE_STATEMENT = TwigCompositeElementType("INVERSE_STATEMENT")

    @JvmField
    val BLOCK_END_STATEMENT = TwigCompositeElementType("BLOCK_END_STATEMENT")

    @JvmField
    val SIMPLE_STATEMENT = TwigCompositeElementType("SIMPLE_STATEMENT")

    @JvmField
    val BLOCK = TwigCompositeElementType("BLOCK")

    val IDENTIFIERS = TokenSet.create(VARIABLE)
    val STRING_LITERALS = TokenSet.create(STRING)
    val COMMENTS = TokenSet.create(COMMENT_CONTENT, COMMENT, UNCLOSED_COMMENT)
    val WHITESPACES = TokenSet.create(WHITE_SPACE)
}
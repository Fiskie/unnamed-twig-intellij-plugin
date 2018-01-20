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
    val INTERPOLATION_OPEN = TwigElementType("INTERPOLATION_OPEN", "twig.parsing.element.expected.interpolation_open")
    @JvmField
    val INTERPOLATION_CLOSE = TwigElementType("INTERPOLATION_CLOSE", "twig.parsing.element.expected.interpolation_close")
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
    val DOT = TwigElementType("DOT", "twig.parsing.element.expected.sep")
    @JvmField
    val OPERATOR = TwigElementType("OPERATOR", "twig.parsing.element.expected.operator")
    @JvmField
    val KEYWORD_OPERATOR = TwigElementType("KEYWORD_OPERATOR", "twig.parsing.element.expected.operator")
    @JvmField
    val NUMBER = TwigElementType("NUMBER", "twig.parsing.element.expected.integer")
    @JvmField
    val STRING = TwigElementType("STRING", "twig.parsing.element.expected.string")
    @JvmField
    val LABEL = TwigElementType("LABEL", "twig.parsing.element.expected.label")
    @JvmField
    val TAG = TwigElementType("TAG", "twig.parsing.element.expected.tag")
    @JvmField
    val INVALID = TwigElementType("INVALID", "twig.parsing.element.expected.invalid")
    @JvmField
    val FILTER_PIPE = TwigElementType("FILTER_PIPE", "twig.parsing.element.expected.FILTER_PIPE")
    @JvmField
    val TEST = TwigElementType("TEST", "twig.parsing.element.expected.test")
    @JvmField
    val COMMA = TwigElementType("COMMA", "twig.parsing.element.expected.comma")
    @JvmField
    val COLON = TwigElementType("COLON", "twig.parsing.element.expected.colon")
    @JvmField
    val SINGLE_QUOTE = TwigElementType("SINGLE_QUOTE", "twig.parsing.element.expected.single_quote")
    @JvmField
    val DOUBLE_QUOTE = TwigElementType("DOUBLE_QUOTE", "twig.parsing.element.expected.double_quote")
    @JvmField
    val COMMENT = TwigElementType("COMMENT", "twig.parsing.element.expected.comment")
    @JvmField
    val NULL = TwigElementType("NULL", "twig.parsing.element.expected.null")

    val EXPRESSION_BLOCK = TwigCompositeElementType("EXPRESSION_BLOCK")
    val BLOCK_WRAPPER = TwigCompositeElementType("BLOCK_WRAPPER")
    val BLOCK_START_STATEMENT = TwigCompositeElementType("BLOCK_START_STATEMENT")
    val INVERSE_STATEMENT = TwigCompositeElementType("INVERSE_STATEMENT")
    val BLOCK_END_STATEMENT = TwigCompositeElementType("BLOCK_END_STATEMENT")
    val SIMPLE_STATEMENT = TwigCompositeElementType("SIMPLE_STATEMENT")
    val BLOCK = TwigCompositeElementType("BLOCK")
    val EXPRESSION = TwigCompositeElementType("EXPRESSION")
    val SUBEXPRESSION = TwigCompositeElementType("SUBEXPRESSION")

    val SINGLE_QUOTED_STRING = TwigCompositeElementType("SINGLE_QUOTED_STRING")
    val DOUBLE_QUOTED_STRING = TwigCompositeElementType("DOUBLE_QUOTED_STRING")

    val VARIABLE = TwigCompositeElementType("VARIABLE")
    val PROPERTY = TwigCompositeElementType("PROPERTY")
    val FUNCTION = TwigCompositeElementType("FUNCTION")
    val METHOD = TwigCompositeElementType("METHOD")
    val ARRAY_ACCESS = TwigCompositeElementType("ARRAY_ACCESS")
    val NAMED_ARGUMENT = TwigCompositeElementType("NAMED_ARGUMENT")

    val HASH = TwigCompositeElementType("HASH")
    val ARRAY = TwigCompositeElementType("ARRAY")

    val BLOCK_LABEL = TwigCompositeElementType("BLOCK_LABEL")
    val MACRO_DECLARATION = TwigCompositeElementType("MACRO_DECLARATION")

    val IDENTIFIERS = TokenSet.create(LABEL)
    val STRING_LITERALS = TokenSet.create(STRING)
    val COMMENTS = TokenSet.create(COMMENT_CONTENT, COMMENT, UNCLOSED_COMMENT)
    val WHITESPACES = TokenSet.create(WHITE_SPACE)
    val TAGS = TokenSet.create(BLOCK_START_STATEMENT, BLOCK_END_STATEMENT, SIMPLE_STATEMENT, INVERSE_STATEMENT, EXPRESSION_BLOCK)
}
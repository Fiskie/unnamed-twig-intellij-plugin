package com.fisk.twig.highlighting

import com.fisk.twig.TwigBundle
import com.fisk.twig.parsing.TwigRawLexer
import com.fisk.twig.parsing.TwigTokenTypes
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.openapi.util.Pair
import com.intellij.psi.tree.IElementType
import java.util.*

class TwigHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer() = TwigRawLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return SyntaxHighlighterBase.pack(keys1[tokenType], keys2[tokenType])
    }

    companion object {
        private val keys1: MutableMap<IElementType, TextAttributesKey>
        private val keys2: Map<IElementType, TextAttributesKey>
        val DISPLAY_NAMES: MutableMap<TextAttributesKey, Pair<String, HighlightSeverity>> = LinkedHashMap()

        private val BLOCKS = TextAttributesKey.createTextAttributesKey(
                "TWIG.BLOCKS",
                DefaultLanguageHighlighterColors.BRACES
        )

        private val BRACES = TextAttributesKey.createTextAttributesKey(
                "TWIG.BRACES",
                DefaultLanguageHighlighterColors.BRACES
        )

        private val BRACKETS = TextAttributesKey.createTextAttributesKey(
                "TWIG.BRACKETS",
                DefaultLanguageHighlighterColors.BRACKETS
        )

        private val COMMAS = TextAttributesKey.createTextAttributesKey(
                "TWIG.COMMAS",
                DefaultLanguageHighlighterColors.COMMA
        )

        private val COMMENTS = TextAttributesKey.createTextAttributesKey(
                "TWIG.COMMENTS",
                DefaultLanguageHighlighterColors.BLOCK_COMMENT
        )

        private val FILTER_PIPES = TextAttributesKey.createTextAttributesKey(
                "TWIG.FILTER_PIPES",
                DefaultLanguageHighlighterColors.DOT
        )

        private val ACCESSORS = TextAttributesKey.createTextAttributesKey(
                "TWIG.ACCESSORS",
                DefaultLanguageHighlighterColors.DOT
        )

        private val KEYWORD_OPERATORS = TextAttributesKey.createTextAttributesKey(
                "TWIG.KEYWORD_OPERATORS",
                DefaultLanguageHighlighterColors.KEYWORD
        )

        private val OPERATORS = TextAttributesKey.createTextAttributesKey(
                "TWIG.OPERATORS",
                DefaultLanguageHighlighterColors.OPERATION_SIGN
        )

        private val PARENTHESES = TextAttributesKey.createTextAttributesKey(
                "TWIG.PARENTHESES",
                DefaultLanguageHighlighterColors.PARENTHESES
        )

        private val STRINGS = TextAttributesKey.createTextAttributesKey(
                "TWIG.STRINGS",
                DefaultLanguageHighlighterColors.STRING
        )

        private val TAGS = TextAttributesKey.createTextAttributesKey(
                "TWIG.TAGS",
                DefaultLanguageHighlighterColors.KEYWORD
        )

        private val TESTS = TextAttributesKey.createTextAttributesKey(
                "TWIG.TESTS",
                DefaultLanguageHighlighterColors.LABEL
        )

        private val VALUES = TextAttributesKey.createTextAttributesKey(
                "TWIG.VALUES",
                DefaultLanguageHighlighterColors.NUMBER
        )

        private val VARIABLES = TextAttributesKey.createTextAttributesKey(
                "TWIG.VARIABLES",
                DefaultLanguageHighlighterColors.LOCAL_VARIABLE
        )

        init {
            keys1 = HashMap()
            keys2 = HashMap()

            keys1.put(TwigTokenTypes.DOT, ACCESSORS)
            keys1.put(TwigTokenTypes.EXPRESSION_OPEN, BLOCKS)
            keys1.put(TwigTokenTypes.EXPRESSION_CLOSE, BLOCKS)
            keys1.put(TwigTokenTypes.STATEMENT_OPEN, BLOCKS)
            keys1.put(TwigTokenTypes.STATEMENT_CLOSE, BLOCKS)
            keys1.put(TwigTokenTypes.LBRACKET, BRACKETS)
            keys1.put(TwigTokenTypes.RBRACKET, BRACKETS)
            keys1.put(TwigTokenTypes.LBRACE, BRACES)
            keys1.put(TwigTokenTypes.RBRACE, BRACES)
            keys1.put(TwigTokenTypes.COMMA, COMMAS)
            keys1.put(TwigTokenTypes.COMMENT_OPEN, COMMENTS)
            keys1.put(TwigTokenTypes.COMMENT_CLOSE, COMMENTS)
            keys1.put(TwigTokenTypes.COMMENT_CONTENT, COMMENTS)
            keys1.put(TwigTokenTypes.UNCLOSED_COMMENT, COMMENTS)
            keys1.put(TwigTokenTypes.FILTER_PIPE, FILTER_PIPES)
            keys1.put(TwigTokenTypes.KEYWORD_OPERATOR, KEYWORD_OPERATORS)
            keys1.put(TwigTokenTypes.EQUALS, OPERATORS)
            keys1.put(TwigTokenTypes.OPERATOR, OPERATORS)
            keys1.put(TwigTokenTypes.LPARENTH, PARENTHESES)
            keys1.put(TwigTokenTypes.RPARENTH, PARENTHESES)
            keys1.put(TwigTokenTypes.STRING, STRINGS)
            keys1.put(TwigTokenTypes.SINGLE_QUOTE, STRINGS)
            keys1.put(TwigTokenTypes.DOUBLE_QUOTE, STRINGS)
            keys1.put(TwigTokenTypes.TAG, TAGS)
            keys1.put(TwigTokenTypes.TEST, TESTS)
            keys1.put(TwigTokenTypes.BOOLEAN, VALUES)
            keys1.put(TwigTokenTypes.NUMBER, VALUES)
            keys1.put(TwigTokenTypes.LABEL, VARIABLES)
            keys1.put(TwigTokenTypes.NULL, KEYWORD_OPERATORS)

            DISPLAY_NAMES.put(ACCESSORS, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.accessors.key"), null))
            DISPLAY_NAMES.put(BLOCKS, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.blocks.key"), null))
            DISPLAY_NAMES.put(BRACKETS, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.brackets.key"), null))
            DISPLAY_NAMES.put(BRACES, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.braces.key"), null))
            DISPLAY_NAMES.put(COMMAS, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.commas.key"), null))
            DISPLAY_NAMES.put(COMMENTS, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.comments.key"), null))
            DISPLAY_NAMES.put(FILTER_PIPES, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.filter_pipes.key"), null))
            DISPLAY_NAMES.put(KEYWORD_OPERATORS, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.keyword_operators.key"), null))
            DISPLAY_NAMES.put(OPERATORS, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.operators.key"), null))
            DISPLAY_NAMES.put(PARENTHESES, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.parentheses.key"), null))
            DISPLAY_NAMES.put(STRINGS, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.strings.key"), null))
            DISPLAY_NAMES.put(TAGS, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.tags.key"), null))
            DISPLAY_NAMES.put(TESTS, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.tests.key"), null))
            DISPLAY_NAMES.put(VALUES, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.values.key"), null))
            DISPLAY_NAMES.put(VARIABLES, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.variables.key"), null))
        }
    }
}

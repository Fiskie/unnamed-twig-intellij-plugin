package com.fisk.twig

import com.fisk.twig.parsing.TwigRawLexer
import com.fisk.twig.parsing.TwigTokenTypes
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.lexer.Lexer
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

        private val BLOCKS = TextAttributesKey.createTextAttributesKey(
                "TWIG.BLOCKS",
                DefaultLanguageHighlighterColors.BRACES
        )

        private val IDENTIFIERS = TextAttributesKey.createTextAttributesKey(
                "TWIG.IDENTIFIERS",
                DefaultLanguageHighlighterColors.KEYWORD
        )

        private val COMMENTS = TextAttributesKey.createTextAttributesKey(
                "TWIG.COMMENTS",
                DefaultLanguageHighlighterColors.BLOCK_COMMENT
        )

        private val OPERATORS = TextAttributesKey.createTextAttributesKey(
                "TWIG.OPERATORS",
                DefaultLanguageHighlighterColors.OPERATION_SIGN
        )

        private val KEYWORDS = TextAttributesKey.createTextAttributesKey(
                "TWIG.KEYWORDS",
                DefaultLanguageHighlighterColors.KEYWORD
        )

        private val VALUES = TextAttributesKey.createTextAttributesKey(
                "TWIG.VALUES",
                DefaultLanguageHighlighterColors.NUMBER
        )

        private val STRINGS = TextAttributesKey.createTextAttributesKey(
                "TWIG.STRINGS",
                DefaultLanguageHighlighterColors.STRING
        )

        private val PARENTHESES = TextAttributesKey.createTextAttributesKey(
                "TWIG.PARENTHESES",
                DefaultLanguageHighlighterColors.PARENTHESES
        )

        private val BRACKETS = TextAttributesKey.createTextAttributesKey(
                "TWIG.BRACKETS",
                DefaultLanguageHighlighterColors.BRACKETS
        )

        private val VARIABLES = TextAttributesKey.createTextAttributesKey(
                "TWIG.VARIABLES",
                DefaultLanguageHighlighterColors.LOCAL_VARIABLE
        )

        init {
            keys1 = HashMap()
            keys2 = HashMap()

            keys1.put(TwigTokenTypes.EXPRESSION_OPEN, BLOCKS)
            keys1.put(TwigTokenTypes.EXPRESSION_CLOSE, BLOCKS)
            keys1.put(TwigTokenTypes.STATEMENT_OPEN, BLOCKS)
            keys1.put(TwigTokenTypes.STATEMENT_CLOSE, BLOCKS)
            keys1.put(TwigTokenTypes.LPARENTH, PARENTHESES)
            keys1.put(TwigTokenTypes.RPARENTH, PARENTHESES)
            keys1.put(TwigTokenTypes.LBRACKET, BRACKETS)
            keys1.put(TwigTokenTypes.RBRACKET, BRACKETS)
            keys1.put(TwigTokenTypes.LBRACE, BRACKETS)
            keys1.put(TwigTokenTypes.RBRACE, BRACKETS)
            keys1.put(TwigTokenTypes.COMMENT_OPEN, COMMENTS)
            keys1.put(TwigTokenTypes.COMMENT_CLOSE, COMMENTS)
            keys1.put(TwigTokenTypes.COMMENT_CONTENT, COMMENTS)
            keys1.put(TwigTokenTypes.UNCLOSED_COMMENT, COMMENTS)
            keys1.put(TwigTokenTypes.EQUALS, OPERATORS)
            keys1.put(TwigTokenTypes.SEP, OPERATORS)
            keys1.put(TwigTokenTypes.TAG, IDENTIFIERS)
            keys1.put(TwigTokenTypes.NUMBER, VALUES)
            keys1.put(TwigTokenTypes.OPERATOR, IDENTIFIERS)
            keys1.put(TwigTokenTypes.BOOLEAN, VALUES)
            keys1.put(TwigTokenTypes.STRING, STRINGS)
            keys1.put(TwigTokenTypes.LABEL, VARIABLES)
        }

        val DISPLAY_NAMES: MutableMap<TextAttributesKey, Pair<String, HighlightSeverity>> = LinkedHashMap()

        init {
            DISPLAY_NAMES.put(BLOCKS, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.blocks.key"), null))
            DISPLAY_NAMES
                    .put(IDENTIFIERS, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.identifiers.key"), null))
            DISPLAY_NAMES.put(COMMENTS, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.comments.key"), null))
            DISPLAY_NAMES.put(OPERATORS, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.operators.key"), null))
            DISPLAY_NAMES.put(VALUES, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.values.key"), null))
            DISPLAY_NAMES.put(STRINGS, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.strings.key"), null))
            DISPLAY_NAMES.put(PARENTHESES, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.parentheses.key"), null))
            DISPLAY_NAMES.put(BRACKETS, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.brackets.key"), null))
            DISPLAY_NAMES.put(VARIABLES, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.variables.key"), null))
            DISPLAY_NAMES.put(KEYWORDS, Pair<String, HighlightSeverity>(TwigBundle.message("twig.page.colors.descriptor.keywords.key"), null))

        }
    }
}

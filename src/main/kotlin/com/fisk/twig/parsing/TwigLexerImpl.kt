package com.fisk.twig.parsing

import com.intellij.lexer.MergeFunction
import com.intellij.lexer.MergingLexerAdapterBase

/**
 * This will merge comment tokens to help the IDE, and merge CONTENT tokens since the lexer is a little broken.
 */
class TwigLexerImpl : MergingLexerAdapterBase(TwigRawLexer()) {
    companion object {
        val MERGE_FUNCTION = MergeFunction { type, originalLexer ->
            if (type == TwigTokenTypes.CONTENT) {
                while (originalLexer.tokenType == TwigTokenTypes.CONTENT) {
                    originalLexer.advance()
                }

                return@MergeFunction TwigTokenTypes.CONTENT
            }

            if (type !== TwigTokenTypes.COMMENT_OPEN) {
                return@MergeFunction type
            }

            if (originalLexer.tokenType === TwigTokenTypes.COMMENT_CONTENT) {
                originalLexer.advance()
            }

            if (originalLexer.tokenType === TwigTokenTypes.COMMENT_CLOSE) {
                originalLexer.advance()
                return@MergeFunction TwigTokenTypes.COMMENT
            }

            if (originalLexer.tokenType == null) {
                return@MergeFunction TwigTokenTypes.UNCLOSED_COMMENT
            }

            if (originalLexer.tokenType === TwigTokenTypes.UNCLOSED_COMMENT) {
                originalLexer.advance()
                return@MergeFunction TwigTokenTypes.UNCLOSED_COMMENT
            }

            type
        }
    }

    override fun getMergeFunction() = MERGE_FUNCTION
}
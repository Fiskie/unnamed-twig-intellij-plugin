package com.fisk.twig.parsing

import com.intellij.lexer.MergeFunction
import com.intellij.lexer.MergingLexerAdapterBase

class TwigMergingLexer : MergingLexerAdapterBase(TwigRawLexer()) {
    override fun getMergeFunction(): MergeFunction {
        return MergeFunction { type, originalLexer ->
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
}
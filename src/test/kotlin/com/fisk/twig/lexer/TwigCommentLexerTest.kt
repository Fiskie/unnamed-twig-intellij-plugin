package com.fisk.twig.lexer

import com.fisk.twig.parsing.TwigTokenTypes.COMMENT_CLOSE
import com.fisk.twig.parsing.TwigTokenTypes.COMMENT_CONTENT
import com.fisk.twig.parsing.TwigTokenTypes.COMMENT_OPEN
import com.fisk.twig.parsing.TwigTokenTypes.UNCLOSED_COMMENT

class TwigCommentLexerTest : TwigLexerTest() {
    fun testComment() {
        // unlike with statements/comments, we have no reason to tokenize whitespace
        val result = tokenize("{# foo #}")
        result.shouldMatchTokenTypes(COMMENT_OPEN, COMMENT_CONTENT, COMMENT_CLOSE)
        result.shouldMatchTokenContent("{#", " foo ", "#}")
    }

    fun testCommentWithWhitespaceControl() {
        // again, unlike with statements/comments, we have no reason to tokenize whitespace
        val result = tokenize("{#- foo -#}")
        result.shouldMatchTokenTypes(COMMENT_OPEN, COMMENT_CONTENT, COMMENT_CLOSE)
        result.shouldMatchTokenContent("{#-", " foo ", "-#}")
    }

    fun testUnformattedCommentWithWhitespaceControl() {
        val result = tokenize("{#-foo-#}")
        result.shouldMatchTokenTypes(COMMENT_OPEN, COMMENT_CONTENT, COMMENT_CLOSE)
        result.shouldMatchTokenContent("{#-", "foo", "-#}")
    }

    fun testUnformattedComment() {
        val result = tokenize("{#foo#}")
        result.shouldMatchTokenTypes(COMMENT_OPEN, COMMENT_CONTENT, COMMENT_CLOSE)
        result.shouldMatchTokenContent("{#", "foo", "#}")
    }

    fun testUnclosedComment() {
        val result = tokenize("{#foo")
        result.shouldMatchTokenTypes(COMMENT_OPEN, UNCLOSED_COMMENT)
        result.shouldMatchTokenContent("{#", "foo")
    }
}
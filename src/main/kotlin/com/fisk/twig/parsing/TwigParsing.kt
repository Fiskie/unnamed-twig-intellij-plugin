package com.fisk.twig.parsing

import com.fisk.twig.TwigBundle
import com.fisk.twig.parsing.TwigTokenTypes.COMMENT_CLOSE
import com.fisk.twig.parsing.TwigTokenTypes.COMMENT_OPEN
import com.fisk.twig.parsing.TwigTokenTypes.CONTENT
import com.fisk.twig.parsing.TwigTokenTypes.EXPRESSION_CLOSE
import com.fisk.twig.parsing.TwigTokenTypes.EXPRESSION_OPEN
import com.fisk.twig.parsing.TwigTokenTypes.INVALID
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_BLOCK
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_CLOSE
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_CLOSE_BLOCK
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_OPEN
import com.fisk.twig.parsing.TwigTokenTypes.STATEMENT_OPEN_BLOCK
import com.fisk.twig.parsing.TwigTokenTypes.TAG
import com.fisk.twig.parsing.TwigTokenTypes.TWIG_BLOCK
import com.fisk.twig.parsing.TwigTokenTypes.UNCLOSED_COMMENT
import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType
import java.util.*

// TODO: handle 'else', which will be used as an inverse tag for 'for' and 'if'
class TwigParsing(val builder: PsiBuilder) {
    val nonStackingTags = setOf("extends", "do", "set")
    val stack = Stack<String>()

    fun parse() {
        do {
            parseRoot(builder)
        } while (!builder.eof())
    }

    fun parseRoot(builder: PsiBuilder) {
        val statementsMarker = builder.mark()

        // parse zero or more statements (empty statements are acceptable)
        while (true) {
            val optionalStatementMarker = builder.mark()
            if (parseBlock(builder)) {
                optionalStatementMarker.drop()
            } else {
                optionalStatementMarker.rollbackTo()
                break
            }
        }

        statementsMarker.done(TWIG_BLOCK)
    }

    fun parseBlock(builder: PsiBuilder) : Boolean {
        if (parseExpression(builder)) {
            return true
        }

        if (parseComment(builder)) {
            return true
        }

        if (parseStatement(builder)) {
            return true
        }

        if (builder.tokenType == CONTENT) {
            // eat non-Twig content
            builder.advanceLexer()
            return true
        }

        return false
    }

    fun parseStatement(builder: PsiBuilder) : Boolean {
        if (builder.tokenType == STATEMENT_OPEN) {
            val statementMarker = builder.mark()

            if (parseOpenBlock(builder)) {
                parseRoot(builder)
                parseCloseBlock(builder)
                statementMarker.done(STATEMENT_BLOCK)
                return true
            }

            statementMarker.drop()
        }

        return false
    }

    fun parseOpenBlock(builder: PsiBuilder) : Boolean {
        val openMarker = builder.mark()
        var tagName: String? = null

        do {
            builder.advanceLexer()

            if (builder.tokenType == TAG) {
                val tag = builder.tokenText

                tag?.let {
                    if (!isEndTag(tag) && !nonStackingTags.contains(tag)) {
                        tagName = tag
                    }
                }
            }
        } while (!(builder.eof() || builder.rawLookup(-1) == STATEMENT_CLOSE))

        if (tagName != null) {
            openMarker.done(STATEMENT_OPEN_BLOCK)
            return true
        } else {
            openMarker.drop()
            return false
        }
    }

    fun parseCloseBlock(builder: PsiBuilder) : Boolean {
        val openMarker = builder.mark()
        var tagName: String? = null

        do {
            builder.advanceLexer()

            if (builder.tokenType == TAG) {
                val tag = builder.tokenText

                tag?.let {
                    if (isEndTag(tag)) {
                        tagName = getStartTag(tag)
                    }
                }
            }
        } while (!(builder.eof() || builder.rawLookup(-1) == STATEMENT_CLOSE))

        if (tagName != null) {
            openMarker.done(STATEMENT_CLOSE_BLOCK)
            return true
        } else {
            openMarker.drop()
            return false
        }
    }

    fun parseExpression(builder: PsiBuilder) : Boolean {
        if (builder.tokenType == EXPRESSION_OPEN) {
            val expressionMarker = builder.mark()

            do {
                builder.advanceLexer()
            } while (!(builder.eof() || builder.rawLookup(-1) == EXPRESSION_CLOSE))

            expressionMarker.done(TwigTokenTypes.EXPRESSION)
            return true
        }

        return false
    }

    fun parseComment(builder: PsiBuilder) : Boolean {
        if (builder.tokenType == COMMENT_OPEN) {
            val commentMarker = builder.mark()

            do {
                builder.advanceLexer()

                if (builder.tokenType === UNCLOSED_COMMENT) {
                    val unclosedCommentMarker = builder.mark()
                    unclosedCommentMarker.error(TwigBundle.message("twig.parsing.comment.unclosed"))
                }
            } while (!(builder.eof() || builder.rawLookup(-1) == COMMENT_CLOSE))

            commentMarker.done(TwigTokenTypes.COMMENT)
            return true
        }

        return false
    }

    /**
     * Tries to parse the given token, marking an error if any other token is found
     */
    protected fun parseLeafToken(builder: PsiBuilder, leafTokenType: IElementType): Boolean {
        val leafTokenMark = builder.mark()
        if (builder.tokenType === leafTokenType) {
            builder.advanceLexer()
            leafTokenMark.done(leafTokenType)
            return true
        } else if (builder.tokenType === INVALID) {
            while (!builder.eof() && builder.tokenType === INVALID) {
                builder.advanceLexer()
            }
            recordLeafTokenError(INVALID, leafTokenMark)
            return false
        } else {
            recordLeafTokenError(leafTokenType, leafTokenMark)
            return false
        }
    }

    private fun recordLeafTokenError(expectedToken: IElementType, unexpectedTokensMarker: PsiBuilder.Marker) {
        if (expectedToken is TwigElementType) {
            unexpectedTokensMarker.error(expectedToken.parseExpectedMessage())
        } else {
            unexpectedTokensMarker.error(TwigBundle.message("twig.parsing.element.expected.invalid"))
        }
    }

    /**
     * Detects whether this tag is closing a block or not
     */
    private fun isEndTag(tag: String) = tag.length > 3 && tag.substring(0, 3) == "end"

    /**
     * Truncates the "end" of an end tag (e.g. endif) to return the start tag (e.g. if)
     */
    private fun getStartTag(tag: String) : String {
        return if (isEndTag(tag)) {
            tag.substring(3)
        } else {
            tag
        }
    }
}
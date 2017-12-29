package com.fisk.twig.parsing

import com.fisk.twig.TwigBundle
import com.fisk.twig.parsing.TwigTokenTypes.CONTENT
import com.fisk.twig.parsing.TwigTokenTypes.COMMENT_CONTENT
import com.fisk.twig.parsing.TwigTokenTypes.INVALID
import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType

class TwigParsing(val builder: PsiBuilder) {
    fun parse() {
        while (!builder.eof()) {
            parseRoot(builder)

            if (builder.eof())
                break

//            val tokenType = builder.tokenType
//            val problemOffset = builder.currentOffset

//            if (builder.currentOffset == problemOffset) {
//                val problemMark = builder.mark()
//                builder.advanceLexer()
//                problemMark.error(TwigBundle.message("twig.parsing.invalid"))
//            }

            builder.advanceLexer()
        }
    }

    fun parseRoot(builder: PsiBuilder) {
        parseContents(builder)
    }

    fun parseContents(builder: PsiBuilder) {
        val statementsMarker = builder.mark()

        while (true) {
            val optionalStatementMarker = builder.mark()

            if (parseStatement(builder)) {
                optionalStatementMarker.drop()
            } else {
                optionalStatementMarker.rollbackTo()
                break
            }
        }

        statementsMarker.done(TwigTokenTypes.STATEMENT)
    }

    fun parseStatement(builder: PsiBuilder): Boolean {
        val tokenType = builder.tokenType

        if (tokenType === CONTENT) {
            builder.advanceLexer() // eat non-HB content
            return true
        }

        if (tokenType === COMMENT_CONTENT) {
            parseLeafToken(builder, TwigTokenTypes.COMMENT_CONTENT)
            return true
        }

        return true
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
}
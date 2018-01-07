package com.fisk.twig.parsing

import com.intellij.lexer.Lexer
import com.intellij.psi.tree.IElementType
import com.intellij.testFramework.PlatformLiteFixture
import junit.framework.Assert
import java.util.*

abstract class TwigLexerTest : PlatformLiteFixture() {
    private lateinit var lexer: Lexer

    override fun setUp() {
        super.setUp()
        lexer = TwigRawLexer()
    }

    internal fun tokenize(string: String): TokenizerResult {
        val tokens = ArrayList<Token>()
        var currentElement: IElementType?

        lexer.start(string)

        do {
            currentElement = lexer.tokenType

            currentElement?.let {
                tokens.add(Token(currentElement, lexer.tokenText))
                lexer.advance()
            }
        } while (currentElement != null)

        return TokenizerResult(tokens)
    }

    internal class Token constructor(val elementType: IElementType, val elementContent: String) {
        override fun toString(): String {
            return elementType.toString() + ": " + elementContent
        }
    }

    internal class TokenizerResult(private val _tokens: List<Token>) {
        /**
         * @param tokenTypes The token types expected for the tokens in this TokenizerResult, in the order they are expected
         */
        fun shouldMatchTokenTypes(vararg tokenTypes: IElementType) {

            // compare tokens as far as we can (which is ideally all of them).  We'll check that
            // these have the same length next - doing the content compare first yields more illuminating failures
            // in the case of a mis-match
            for (i in 0 until Math.min(_tokens.size, tokenTypes.size)) {
                Assert.assertEquals("Bad token at position " + i, tokenTypes[i], _tokens[i].elementType)
            }

            Assert.assertEquals(tokenTypes.size, _tokens.size)
        }

        /**
         * @param tokenContent The content string expected for the tokens in this TokenizerResult, in the order they are expected
         */
        fun shouldMatchTokenContent(vararg tokenContent: String) {

            // compare tokens as far as we can (which is ideally all of them).  We'll check that
            // these have the same length next - doing the content compare first yields more illuminating failures
            // in the case of a mis-match
            for (i in 0 until Math.min(_tokens.size, tokenContent.size)) {
                Assert.assertEquals(tokenContent[i], _tokens[i].elementContent)
            }

            Assert.assertEquals(tokenContent.size, _tokens.size)
        }

        /**
         * Convenience method for validating a specific token in this TokenizerResult
         */
        fun shouldBeToken(tokenPosition: Int, tokenType: IElementType, tokenContent: String) {
            val token = _tokens[tokenPosition]

            Assert.assertEquals(tokenType, token.elementType)
            Assert.assertEquals(tokenContent, token.elementContent)
        }
    }
}
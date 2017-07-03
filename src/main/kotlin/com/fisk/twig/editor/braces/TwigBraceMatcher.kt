package com.fisk.twig.editor.braces

import com.fisk.twig.parsing.TwigTokenTypes
import com.intellij.codeInsight.highlighting.BraceMatcher
import com.intellij.openapi.editor.highlighter.HighlighterIterator
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import java.util.*

class TwigBraceMatcher : BraceMatcher {
    override fun isPairBraces(tokenType1: IElementType, tokenType2: IElementType): Boolean {
        return PAIRS.contains(Pair(tokenType1, tokenType2)) or PAIRS.contains(Pair(tokenType2, tokenType1))
    }

    override fun isLBraceToken(iterator: HighlighterIterator, fileText: CharSequence, fileType: FileType): Boolean {
        return PAIRS.map { it.first }.contains(iterator.tokenType)
    }

    override fun isRBraceToken(iterator: HighlighterIterator, fileText: CharSequence, fileType: FileType): Boolean {
        return PAIRS.map { it.second }.contains(iterator.tokenType)
    }

    override fun getBraceTokenGroupId(tokenType: IElementType): Int {
        return 1
    }

    override fun isStructuralBrace(iterator: HighlighterIterator, text: CharSequence, fileType: FileType): Boolean {
        return false
    }

    override fun getOppositeBraceTokenType(type: IElementType): IElementType? {
        return null
    }

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean {
        return true
    }

    override fun getCodeConstructStart(file: PsiFile, openingBraceOffset: Int): Int {
        return openingBraceOffset
    }

    companion object {
        private val PAIRS = HashSet<Pair<IElementType, IElementType>>()

        init {
            PAIRS.add(Pair(TwigTokenTypes.COMMENT_OPEN, TwigTokenTypes.COMMENT_CLOSE))
            PAIRS.add(Pair(TwigTokenTypes.EXPRESSION_OPEN, TwigTokenTypes.EXPRESSION_CLOSE))
            PAIRS.add(Pair(TwigTokenTypes.STATEMENT_OPEN, TwigTokenTypes.STATEMENT_CLOSE))
            PAIRS.add(Pair(TwigTokenTypes.OPEN_SEXPR, TwigTokenTypes.CLOSE_SEXPR))
            PAIRS.add(Pair(TwigTokenTypes.OPEN_LIST, TwigTokenTypes.CLOSE_LIST))
            PAIRS.add(Pair(TwigTokenTypes.OPEN_DICT, TwigTokenTypes.CLOSE_DICT))
        }
    }
}

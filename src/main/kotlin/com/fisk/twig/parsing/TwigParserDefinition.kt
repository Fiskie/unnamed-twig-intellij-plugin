package com.fisk.twig.parsing

import com.fisk.twig.TwigLanguage
import com.fisk.twig.psi.TwigPsiFile
import com.fisk.twig.psi.impl.*
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.PsiFileStub
import com.intellij.psi.tree.IStubFileElementType

class TwigParserDefinition : ParserDefinition {
    override fun createParser(project: Project?) = TwigParser()

    override fun createFile(viewProvider: FileViewProvider?) = TwigPsiFile(viewProvider!!)

    override fun spaceExistanceTypeBetweenTokens(left: ASTNode?, right: ASTNode?): ParserDefinition.SpaceRequirements {
        return ParserDefinition.SpaceRequirements.MAY
    }

    override fun getStringLiteralElements() = TwigTokenTypes.STRING_LITERALS

    override fun getCommentTokens() = TwigTokenTypes.COMMENTS

    override fun getWhitespaceTokens() = TwigTokenTypes.WHITESPACES

    override fun getFileNodeType(): IStubFileElementType<PsiFileStub<TwigPsiFile>> {
        return IStubFileElementType("FILE", TwigLanguage.INSTANCE)
    }

    override fun createLexer(project: Project?) = TwigLexerImpl()

    override fun createElement(node: ASTNode?): PsiElement {
        node?.let {
            val type = node.elementType

            if (type == TwigTokenTypes.EXPRESSION) {
                return TwigExpressionImpl(node)
            }

            if (type == TwigTokenTypes.COMMENT) {
                return TwigCommentImpl(node)
            }

            if (type == TwigTokenTypes.BLOCK) {
                return TwigBlockImpl(node)
            }

            if (type == TwigTokenTypes.BLOCK_WRAPPER) {
                return TwigBlockWrapperImpl(node)
            }

            if (type == TwigTokenTypes.LABEL) {
                return TwigLabelImpl(node)
            }

            if (type == TwigTokenTypes.STRING) {
                return TwigStringImpl(node)
            }

            if (type == TwigTokenTypes.TAG) {
                return TwigTagImpl(node)
            }

            if (type == TwigTokenTypes.BLOCK_START_STATEMENT) {
                return TwigBlockStartStatementImpl(node)
            }

            if (type == TwigTokenTypes.BLOCK_END_STATEMENT) {
                return TwigBlockEndStatementImpl(node)
            }

            if (type == TwigTokenTypes.INVERSE_STATEMENT) {
                return TwigInverseStatementImpl(node)
            }

            if (type == TwigTokenTypes.SIMPLE_STATEMENT) {
                return TwigSimpleStatementImpl(node)
            }

            if (type == TwigTokenTypes.EXPRESSION_BLOCK) {
                return TwigExpressionBlockImpl(node)
            }
        }

        return TwigPsiElementImpl(node!!)
    }
}
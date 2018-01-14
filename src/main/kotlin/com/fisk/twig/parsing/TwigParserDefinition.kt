package com.fisk.twig.parsing

import com.fisk.twig.TwigLanguage
import com.fisk.twig.psi.TwigPsiFile
import com.fisk.twig.psi.impl.*
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
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

    override fun createElement(node: ASTNode?) = when (node?.elementType) {
        TwigTokenTypes.EXPRESSION -> TwigExpressionImpl(node)
        TwigTokenTypes.COMMENT -> TwigCommentImpl(node)
        TwigTokenTypes.BLOCK -> TwigBlockImpl(node)
        TwigTokenTypes.BLOCK_WRAPPER -> TwigBlockWrapperImpl(node)
        TwigTokenTypes.LABEL -> TwigLabelImpl(node)
        TwigTokenTypes.STRING -> TwigStringImpl(node)
        TwigTokenTypes.TAG -> TwigTagImpl(node)
        TwigTokenTypes.BLOCK_START_STATEMENT -> TwigBlockStartStatementImpl(node)
        TwigTokenTypes.BLOCK_END_STATEMENT -> TwigBlockEndStatementImpl(node)
        TwigTokenTypes.INVERSE_STATEMENT -> TwigInverseStatementImpl(node)
        TwigTokenTypes.SIMPLE_STATEMENT -> TwigSimpleStatementImpl(node)
        TwigTokenTypes.EXPRESSION_BLOCK -> TwigExpressionBlockImpl(node)
        TwigTokenTypes.VARIABLE -> TwigVariableImpl(node)
        TwigTokenTypes.PROPERTY -> TwigPropertyImpl(node)
        TwigTokenTypes.MACRO_DECLARATION -> TwigMacroDeclarationImpl(node)
        TwigTokenTypes.BLOCK_LABEL -> TwigBlockLabelImpl(node)
        else -> TwigPsiElementImpl(node!!)
    }
}
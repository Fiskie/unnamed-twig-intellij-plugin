package com.fisk.twig.parsing

import com.fisk.twig.TwigLanguage
import com.fisk.twig.psi.TwigBlock
import com.fisk.twig.psi.TwigPsiFile
import com.fisk.twig.psi.TwigStatementClose
import com.fisk.twig.psi.impl.*
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.PsiFileStub
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.IStubFileElementType
import com.intellij.psi.tree.TokenSet

class TwigParserDefinition : ParserDefinition {
    override fun createParser(project: Project?) = TwigParser()

    override fun createFile(viewProvider: FileViewProvider?) = TwigPsiFile(viewProvider!!)

    override fun spaceExistanceTypeBetweenTokens(left: ASTNode?, right: ASTNode?): ParserDefinition.SpaceRequirements {
        return ParserDefinition.SpaceRequirements.MAY
    }

    override fun getStringLiteralElements() = TwigTokenTypes.STRING_LITERALS

    override fun getCommentTokens() = TwigTokenTypes.COMMENTS

    override fun getWhitespaceTokens() = TwigTokenTypes.WHITESPACES

    override fun getFileNodeType(): IFileElementType {
        return IStubFileElementType<PsiFileStub<TwigPsiFile>>("FILE", TwigLanguage.INSTANCE)
    }

    override fun createLexer(project: Project?) = TwigMergingLexer()

    override fun createElement(node: ASTNode?) : PsiElement {
        node?.let {
            val type = node.elementType

            if (type == TwigTokenTypes.STATEMENT_OPEN) {
                return TwigStatementOpenImpl(node)
            }

            if (type == TwigTokenTypes.STATEMENT_CLOSE) {
                return TwigStatementCloseImpl(node)
            }

            if (type == TwigTokenTypes.STATEMENT) {
                return TwigStatementImpl(node)
            }

            if (type == TwigTokenTypes.COMMENT) {
                return TwigCommentImpl(node)
            }

            if (type == TwigTokenTypes.BLOCK) {
                return TwigBlockImpl(node)
            }

            if (type == TwigTokenTypes.VARIABLE) {
                return TwigVariableImpl(node)
            }
        }

        return TwigPsiElementImpl(node!!)
    }
}
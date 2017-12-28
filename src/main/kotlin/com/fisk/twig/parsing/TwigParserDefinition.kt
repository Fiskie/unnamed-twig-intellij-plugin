package com.fisk.twig.parsing

import com.fisk.twig.TwigLanguage
import com.fisk.twig.psi.TwigPsiFile
import com.fisk.twig.psi.impl.TwigPsiElementImpl
import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.PsiFileStub
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.IStubFileElementType

class TwigParserDefinition : ParserDefinition {
    override fun createParser(project: Project?) = TwigParser()

    override fun createFile(viewProvider: FileViewProvider?) = TwigPsiFile(viewProvider!!)

    override fun spaceExistanceTypeBetweenTokens(left: ASTNode?, right: ASTNode?): ParserDefinition.SpaceRequirements {
        return ParserDefinition.SpaceRequirements.MAY
    }

    override fun getStringLiteralElements() = TwigTokenTypes.STRING_LITERALS

    override fun getCommentTokens() = TwigTokenTypes.COMMENTS

    override fun getFileNodeType(): IFileElementType {
        return IStubFileElementType<PsiFileStub<TwigPsiFile>>("FILE", TwigLanguage.INSTANCE)
    }

    override fun createLexer(project: Project?): Lexer {
        return TwigRawLexer()
    }

    override fun createElement(node: ASTNode?): PsiElement {
        return TwigPsiElementImpl(node!!)
    }
}
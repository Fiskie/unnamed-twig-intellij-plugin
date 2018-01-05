package com.fisk.twig.psi.util

import com.fisk.twig.file.TwigFileType
import com.fisk.twig.psi.TwigPsiElement
import com.fisk.twig.psi.TwigPsiFile
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory

object TwigElementFactory {
    fun createProperty(project: Project, name: String): TwigPsiElement {
        val file = createFile(project, name)
        return file.firstChild as TwigPsiElement
    }

    fun createFile(project: Project, text: String): TwigPsiFile {
        val name = "dummy.simple"
        return PsiFileFactory.getInstance(project).createFileFromText(name, TwigFileType.INSTANCE, text) as TwigPsiFile
    }
}
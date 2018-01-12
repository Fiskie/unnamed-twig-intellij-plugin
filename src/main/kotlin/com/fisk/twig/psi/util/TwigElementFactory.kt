package com.fisk.twig.psi.util

import com.fisk.twig.file.TwigFileType
import com.fisk.twig.psi.TwigPsiElement
import com.fisk.twig.psi.TwigPsiFile
import com.fisk.twig.psi.TwigVariable
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil

object TwigElementFactory {
    fun createVariable(project: Project, name: String): TwigPsiElement {
        val file = createFile(project, "{{$name}}")
        return PsiTreeUtil.findChildOfType(file.firstChild, TwigVariable::class.java) as TwigPsiElement
    }

    fun createFile(project: Project, text: String): TwigPsiFile {
        val name = "dummy.simple"
        return PsiFileFactory.getInstance(project).createFileFromText(name, TwigFileType.INSTANCE, text) as TwigPsiFile
    }
}
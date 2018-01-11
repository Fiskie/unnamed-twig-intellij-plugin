package com.fisk.twig.psi.util

import com.fisk.twig.file.TwigFileType
import com.fisk.twig.psi.*
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.indexing.FileBasedIndex
import java.util.*

object TwigPsiUtil {
    /**
     * Tests to see if the given element is not the "root" statements expression of the grammar
     */
    fun isNonRootBlockElement(element: PsiElement): Boolean {
        val statementsParent = PsiTreeUtil.findFirstParent(element, true) { element1 -> element1 != null && element1 is TwigBlock }

        // we're a non-root statements if we're of type statements, and we have a statements parent
        return element is TwigBlock && statementsParent != null
    }

    fun findLabels(project: Project, key: String): List<TwigLabel> {
        val result = ArrayList<TwigLabel>()
        val virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, TwigFileType.INSTANCE,
                GlobalSearchScope.allScope(project))

        virtualFiles
                .mapNotNull { PsiManager.getInstance(project).findFile(it) as TwigPsiFile? }
                .mapNotNull { PsiTreeUtil.getChildrenOfType(it, TwigLabel::class.java) }
                .forEach { it.filterTo(result) { key == it.name } }

        return result
    }

    fun findLabels(project: Project): List<TwigLabel> {
        val result = ArrayList<TwigLabel>()
        val virtualFiles = FileBasedIndex.getInstance().getContainingFiles<FileType, Void>(FileTypeIndex.NAME, TwigFileType.INSTANCE,
                GlobalSearchScope.allScope(project))

        virtualFiles
                .mapNotNull { PsiManager.getInstance(project).findFile(it) as TwigPsiFile? }
                .mapNotNull { PsiTreeUtil.getChildrenOfType(it, TwigLabel::class.java) }
                .forEach { result.addAll(it) }
        return result
    }

    fun findOpposingEndStatement(statement: TwigBlockStartStatement): TwigBlockEndStatement? {
        return statement.parent.lastChild as? TwigBlockEndStatement
    }
}
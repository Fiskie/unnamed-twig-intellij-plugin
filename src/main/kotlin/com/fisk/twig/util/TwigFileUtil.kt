package com.fisk.twig.util

import com.fisk.twig.psi.TwigPsiFile
import com.fisk.twig.psi.TwigSimpleStatement
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.util.indexing.FileBasedIndex

object TwigFileUtil {
    fun getParents(file: PsiFile): Collection<VirtualFile> {
        val files = HashSet<VirtualFile>()

        if (file !is TwigPsiFile) {
            return files
        }

        file.firstChild.children.forEach {
            if (it is TwigSimpleStatement) {
                if (it.tag?.name == "include" || it.tag?.name == "use") {

                }
            }
        }

        return files
    }
}
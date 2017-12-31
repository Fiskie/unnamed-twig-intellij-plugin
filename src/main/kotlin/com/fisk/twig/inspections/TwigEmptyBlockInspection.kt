package com.fisk.twig.inspections


import com.fisk.twig.TwigBundle
import com.fisk.twig.psi.TwigStatement
import com.fisk.twig.psi.TwigStatementOpenBrackets
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.containers.ContainerUtil

class TwigEmptyBlockInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement?) {
                if (element !is TwigStatement) {
                    return
                }

                val name = element.getName()

                name?.let {
                    holder.registerProblem(element, TwigBundle.message("twig.block.mismatch.inspection.empty.block", name))
                }
            }
        }
    }

    companion object {
        private val HELPERS_WITH_ARGUMENTS = ContainerUtil.immutableSet("if", "for", "in")
    }
}

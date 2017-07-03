package com.fisk.twig.inspections


import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.util.containers.ContainerUtil

class TwigEmptyBlockInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor() {

            override fun visitElement(element: PsiElement?) {

            }
        }
    }

    companion object {
        private val HELPERS_WITH_ARGUMENTS = ContainerUtil.immutableSet("if", "for", "in")
    }
}

package com.fisk.twig.reference

import com.fisk.twig.config.TwigIcons
import com.fisk.twig.psi.util.TwigPsiUtil
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.ResolveResult
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElementResolveResult



class TwigReference(psi: PsiElement, textRange: TextRange) : PsiReferenceBase<PsiElement>(psi, textRange), PsiPolyVariantReference {
    val key = psi.text.substring(textRange.startOffset, textRange.endOffset)

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val project = myElement.project
        val labels = TwigPsiUtil.findLabels(project, key)
        val results = labels.map { PsiElementResolveResult(it) }
        return results.toTypedArray()
    }

    override fun resolve(): PsiElement? {
        val resolveResults = multiResolve(false)
        return if (resolveResults.size == 1) resolveResults[0].element else null
    }

    override fun getVariants(): Array<Any> {
        val project = myElement.project
        val labels = TwigPsiUtil.findLabels(project)
        val variants = labels
                .filter { it.name != null && it.name?.length!! > 0 }
                .map { LookupElementBuilder.create(it).withIcon(TwigIcons.file_icon).withTypeText(it.containingFile.name) }
        return variants.toTypedArray()
    }
}
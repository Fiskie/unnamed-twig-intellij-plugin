package com.fisk.twig.editor.breadcrumbs

import com.fisk.twig.TwigLanguage
import com.fisk.twig.psi.TwigBlockWrapper
import com.fisk.twig.psi.TwigExpressionBlock
import com.fisk.twig.psi.TwigPsiElement
import com.intellij.lang.Language
import com.intellij.lang.html.HTMLLanguage
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlTag
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider

class TwigBreadcrumbsInfoProvider : BreadcrumbsProvider {
    override fun getLanguages(): Array<Language> {
        return arrayOf(TwigLanguage.INSTANCE, HTMLLanguage.INSTANCE)
    }

    override fun getElementInfo(element: PsiElement): String {
        if (element is XmlTag) {
            return element.name
        } else if (element is TwigPsiElement) {
            return element.getName() ?: ""
        }

        return ""
    }

    override fun acceptElement(element: PsiElement): Boolean {
        return element is XmlTag || element is TwigBlockWrapper || element is TwigExpressionBlock
    }

    override fun getParent(element: PsiElement): PsiElement? {
        return element.parent
    }
}
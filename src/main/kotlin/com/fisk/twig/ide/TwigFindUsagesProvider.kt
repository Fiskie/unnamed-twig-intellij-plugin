package com.fisk.twig.ide

import com.fisk.twig.parsing.TwigMergingLexer
import com.fisk.twig.parsing.TwigTokenTypes
import com.fisk.twig.psi.TwigPsiElement
import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement

class TwigFindUsagesProvider : FindUsagesProvider {
    override fun getWordsScanner(): WordsScanner? {
        return DefaultWordsScanner(TwigMergingLexer(), TwigTokenTypes.IDENTIFIERS, TwigTokenTypes.COMMENTS, TwigTokenTypes.STRING_LITERALS)
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return getDescriptiveName(element)
    }

    override fun getDescriptiveName(element: PsiElement): String {
        if (element is TwigPsiElement) {
            return element.getName() ?: ""
        }

        return ""
    }

    override fun getType(element: PsiElement): String {
        return element.toString()
    }

    override fun getHelpId(psiElement: PsiElement): String? {
        return "reference.dialogs.findUsages.other"
    }

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return (psiElement is TwigPsiElement)
    }
}
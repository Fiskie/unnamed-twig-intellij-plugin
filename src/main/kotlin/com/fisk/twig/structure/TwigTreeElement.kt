package com.fisk.twig.structure

import com.fisk.twig.psi.TwigBlock
import com.fisk.twig.psi.TwigPsiElement
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase
import com.intellij.psi.PsiElement
import com.intellij.util.ReflectionUtil
import java.util.*

internal class TwigTreeElement private constructor(private val myElement: TwigPsiElement) : PsiTreeElementBase<TwigPsiElement>(myElement) {
    override fun getChildrenBase() = getStructureViewTreeElements(myElement)

    override fun getPresentableText() = myElement.getName()

    override fun getIcon(open: Boolean) = myElement.getIcon(0)!!

    companion object {
        fun getStructureViewTreeElements(psiElement: PsiElement): List<StructureViewTreeElement> {
            val children = ArrayList<StructureViewTreeElement>()

            for (child in psiElement.children) {
                if (child !is TwigPsiElement) {
                    continue
                }

                if (child is TwigBlock) {
                    // TwigBlock elements transparently wrap other elements, so we don't add
                    // this element to the tree, but we add its children
                    children.addAll(TwigTreeElement(child as TwigPsiElement).childrenBase)
                }

                for (suitableClass in TwigStructureViewModel.ourSuitableClasses) {
                    if (ReflectionUtil.isAssignable(suitableClass, child.javaClass)) {
                        children.add(TwigTreeElement(child))
                        break
                    }
                }
            }

            return children
        }
    }
}

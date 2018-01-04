package com.fisk.twig.parsing

import com.fisk.twig.TwigLanguage
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls

data class TwigCompositeElementType(@NonNls private val debugName: String) : IElementType(debugName, TwigLanguage.INSTANCE) {
    override fun toString() = "Twig:" + super.toString()
}
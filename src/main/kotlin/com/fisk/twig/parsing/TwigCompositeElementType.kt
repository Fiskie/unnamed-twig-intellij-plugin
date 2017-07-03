package com.fisk.twig.parsing

import com.fisk.twig.TwigLanguage
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls

data class TwigCompositeElementType(@NonNls val debugName: String) : IElementType(debugName, TwigLanguage.INSTANCE)
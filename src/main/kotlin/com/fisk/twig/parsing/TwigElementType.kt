package com.fisk.twig.parsing

import com.fisk.twig.TwigBundle
import com.fisk.twig.TwigLanguage
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls

/**
 * @param parseExpectedMessageKey Key to the [TwigBundle] message to show the user when the parsing
 * *                                expected this token, but found something else.
 */
data class TwigElementType(
        @NonNls val debugName: String,
        @NonNls val parseExpectedMessageKey: String
) : IElementType(debugName, TwigLanguage.INSTANCE) {
    fun parseExpectedMessage(): String {
        return TwigBundle.message(parseExpectedMessageKey)
    }
}
package com.fisk.twig.psi.util

import com.fisk.twig.TwigTagUtil

data class StatementResult(val match: Boolean, val tagName: String = "") {
    val normalisedTagName: String
        get() {
            return TwigTagUtil.normaliseTag(tagName)
        }
}
package com.fisk.twig.psi.util

import com.fisk.twig.TwigTagUtils

data class StatementResult(val match: Boolean, val tagName: String = "") {
    val normalisedTagName: String
        get() {
            return TwigTagUtils.normaliseTag(tagName)
        }
}
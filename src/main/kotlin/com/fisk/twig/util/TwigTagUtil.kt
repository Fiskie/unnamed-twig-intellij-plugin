package com.fisk.twig.util

object TwigTagUtil {
    /**
     * The base set of block tags that will be used to preemptively add PSI block nodes for unclosed tags
     */
    val BASE_BLOCK_TAGS = setOf("if", "for", "block", "embed", "spaceless", "verbatim", "sandbox", "autoescape", "filter", "macro")
    val INVERSE_TAGS = setOf("else", "elseif")
    val INVERSE_ALLOWED = setOf("if", "for")

    /**
     * Detects whether this tag is closing a block or not
     */
    fun isEndTag(tag: String) = tag.length > 3 && tag.substring(0, 3) == "end"

    /**
     * Truncates the "end" of an end tag (e.g. endif) to return the start tag (e.g. if)
     */
    fun normaliseTag(tag: String) = when (isEndTag(tag)) {
        true -> tag.substring(3)
        false -> tag
    }

    fun isDefaultBlockTag(tag: String): Boolean {
        return BASE_BLOCK_TAGS.contains(tag)
    }

    fun isInverseTag(tag: String): Boolean {
        return INVERSE_TAGS.contains(tag)
    }

    /**
     * Returns true if an inverse statement can be used for this tag
     * todo: better approach needed, 'for' can only take else, not elseif. annotator won't be correct
     */
    fun allowsInverseTag(tag: String): Boolean {
        return INVERSE_ALLOWED.contains(tag)
    }
}
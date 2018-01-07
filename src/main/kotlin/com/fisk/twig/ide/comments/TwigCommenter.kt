package com.fisk.twig.ide.comments

import com.intellij.lang.Commenter

/**
 * Commenter for native Twig comments: <pre>{# comment #}</pre>
 */
internal class TwigCommenter : Commenter {
    override fun getLineCommentPrefix() = null
    override fun getBlockCommentPrefix() = "{#"
    override fun getBlockCommentSuffix() = "#}"
    override fun getCommentedBlockCommentPrefix() = null
    override fun getCommentedBlockCommentSuffix() = null
}

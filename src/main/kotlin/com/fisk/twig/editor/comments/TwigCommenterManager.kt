package com.fisk.twig.editor.comments

import com.fisk.twig.TwigLanguage
import com.fisk.twig.config.TwigConfig
import com.intellij.lang.Commenter
import com.intellij.lang.LanguageCommenters

class TwigCommenterManager : Commenter {
    override fun getLineCommentPrefix(): String? {
        val commenter = commenter
        return commenter?.lineCommentPrefix
    }

    override fun getBlockCommentPrefix(): String? {
        val commenter = commenter
        return commenter?.blockCommentPrefix
    }

    override fun getBlockCommentSuffix(): String? {
        val commenter = commenter
        return commenter?.blockCommentSuffix
    }

    override fun getCommentedBlockCommentPrefix(): String? {
        val commenter = commenter
        return commenter?.commentedBlockCommentPrefix
    }

    override fun getCommentedBlockCommentSuffix(): String? {
        val commenter = commenter
        return commenter?.commentedBlockCommentSuffix
    }

    companion object {
        private val ourTwigCommenter = TwigCommenter()

        private val commenter: Commenter?
            get() {
                var commenterLanguage = TwigConfig.commenterLanguage
                if (commenterLanguage == null) {
                    commenterLanguage = TwigLanguage.defaultTemplateLang.language
                } else if (commenterLanguage.isKindOf(TwigLanguage.INSTANCE)) {
                    return ourTwigCommenter
                }

                return LanguageCommenters.INSTANCE.forLanguage(commenterLanguage)
            }
    }
}

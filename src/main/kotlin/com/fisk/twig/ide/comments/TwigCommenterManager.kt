package com.fisk.twig.ide.comments

import com.fisk.twig.TwigLanguage
import com.fisk.twig.config.TwigConfig
import com.intellij.lang.Commenter
import com.intellij.lang.LanguageCommenters

class TwigCommenterManager : Commenter {
    override fun getLineCommentPrefix(): String? {
        return commenter?.lineCommentPrefix
    }

    override fun getBlockCommentPrefix(): String? {
        return commenter?.blockCommentPrefix
    }

    override fun getBlockCommentSuffix(): String? {
        return commenter?.blockCommentSuffix
    }

    override fun getCommentedBlockCommentPrefix(): String? {
        return commenter?.commentedBlockCommentPrefix
    }

    override fun getCommentedBlockCommentSuffix(): String? {
        return commenter?.commentedBlockCommentSuffix
    }

    companion object {
        private val twigCommenter = TwigCommenter()

        private val commenter: Commenter?
            get() {
                var commenterLanguage = TwigConfig.commenterLanguage
                if (commenterLanguage == null) {
                    commenterLanguage = TwigLanguage.defaultTemplateLang.language
                } else if (commenterLanguage.isKindOf(TwigLanguage.INSTANCE)) {
                    return twigCommenter
                }

                return LanguageCommenters.INSTANCE.forLanguage(commenterLanguage)
            }
    }
}

package com.fisk.twig.config

import com.intellij.lang.html.HTMLLanguage

/**
 * Formalizes the properties which we will persist using [com.intellij.ide.util.PropertiesComponent]
 */
enum class Property {
    AUTO_COLLAPSE_BLOCKS {
        override val stringName: String
            get() = "TwigAutoCollapseBlocks"

        override val default: String
            get() = DISABLED
    };

    /**
     * The String which will actually be persisted in a user's properties using [com.intellij.ide.util.PropertiesComponent].
     *
     *
     * This value must be unique amongst Property entries
     *
     *
     * IMPORTANT: these should probably never change so that we don't lose a user's preferences between releases.
     */
    abstract val stringName: String

    /**
     * The default/initial value for a user
     */
    abstract val default: String

    companion object {
        val ENABLED = "enabled"
        val DISABLED = "disabled"
    }
}

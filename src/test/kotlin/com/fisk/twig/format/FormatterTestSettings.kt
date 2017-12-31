package com.fisk.twig.format

import com.fisk.twig.config.TwigConfig
import com.intellij.openapi.fileTypes.StdFileTypes
import com.intellij.psi.codeStyle.CodeStyleSettings

/**
 * Provides standard formatter test settings for the formatter fixture tests
 */
class FormatterTestSettings(private val mySettings: CodeStyleSettings) {
    private var myPrevFormatSetting: Boolean = false
    private var myPrevIndentSize: Int = 0
    private var myPrevDoNotIndentSetting: String? = null

    fun setUp() {
        myPrevFormatSetting = TwigConfig.isFormattingEnabled
        TwigConfig.isFormattingEnabled = true

        myPrevIndentSize = mySettings.getIndentOptions(StdFileTypes.HTML).INDENT_SIZE
        mySettings.getIndentOptions(StdFileTypes.HTML).INDENT_SIZE = 4

        myPrevDoNotIndentSetting = mySettings.HTML_DO_NOT_INDENT_CHILDREN_OF
        mySettings.HTML_DO_NOT_INDENT_CHILDREN_OF = ""
    }

    fun tearDown() {
        TwigConfig.isFormattingEnabled = myPrevFormatSetting
        mySettings.getIndentOptions(StdFileTypes.HTML).INDENT_SIZE = myPrevIndentSize
        mySettings.HTML_DO_NOT_INDENT_CHILDREN_OF = myPrevDoNotIndentSetting
    }
}

/*
 * Use of this source code is governed by the MIT license that can be
 * found in the LICENSE file.
 */

package com.fisk.twig.ide.pages

import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings

class TwigCodeStyleSettings(container: CodeStyleSettings) :
    CustomCodeStyleSettings(TwigCodeStyleSettings::class.java.simpleName, container) {

    @JvmField var SPACES_IN_EXPRESSION_TAGS = true
    @JvmField var SPACES_IN_STATEMENT_TAGS = true
    @JvmField var MIN_NUMBER_OF_BLANKS_BETWEEN_ITEMS = 1
}

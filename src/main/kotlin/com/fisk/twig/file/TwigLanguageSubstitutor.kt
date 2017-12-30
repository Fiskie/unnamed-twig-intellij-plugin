package com.fisk.twig.file

import com.fisk.twig.TwigLanguage
import com.fisk.twig.config.TwigConfig
import com.intellij.ide.highlighter.HtmlFileType
import com.intellij.lang.Language
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.LanguageSubstitutor
import com.intellij.testFramework.LightVirtualFile

class TwigLanguageSubstitutor : LanguageSubstitutor() {
    override fun getLanguage(file: VirtualFile, project: Project): Language? {
        if (TwigConfig.shouldOpenHtmlAsTwig(project) && file.fileType == HtmlFileType.INSTANCE) {
            if (file is LightVirtualFile) {
                return null
            }

            return TwigLanguage.INSTANCE
        }

        return null
    }
}
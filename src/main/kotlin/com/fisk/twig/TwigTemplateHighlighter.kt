package com.fisk.twig

import com.fisk.twig.parsing.TwigTokenTypes
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.ex.util.LayerDescriptor
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.StdFileTypes
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings

class TwigTemplateHighlighter(
        project: Project?,
        virtualFile: VirtualFile?,
        colors: EditorColorsScheme
) : LayeredLexerEditorHighlighter(TwigHighlighter(), colors) {
    init {
        // create main highlighter

        // highlighter for outer lang
        var type: FileType? = null
        if (project == null || virtualFile == null) {
            type = StdFileTypes.PLAIN_TEXT
        } else {
            val language = TemplateDataLanguageMappings.getInstance(project).getMapping(virtualFile)
            if (language != null) type = language.associatedFileType
            if (type == null) type = TwigLanguage.defaultTemplateLang
        }

        // deprecated in IDEA 12, still needed in IDEA 11 TODO remove when IDEA 11 support is dropped
        val outerHighlighter = SyntaxHighlighter.PROVIDER.create(type!!, project, virtualFile)

        registerLayer(TwigTokenTypes.CONTENT, LayerDescriptor(outerHighlighter, ""))
    }
}


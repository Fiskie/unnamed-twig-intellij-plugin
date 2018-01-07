package com.fisk.twig.structure

import com.fisk.twig.TwigLanguage
import com.fisk.twig.file.TwigFileType
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder
import com.intellij.ide.structureView.impl.StructureViewComposite
import com.intellij.ide.structureView.impl.TemplateLanguageStructureViewBuilder
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.psi.PsiFile


internal class TwigStructureViewFactory : PsiStructureViewFactory {
    override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder? {
        return object : TemplateLanguageStructureViewBuilder(psiFile) {
            override fun createMainView(fileEditor: FileEditor?, mainFile: PsiFile?): StructureViewComposite.StructureViewDescriptor? {
                if (!psiFile.isValid) {
                    return null
                }

                val builder = object : TreeBasedStructureViewBuilder() {
                    override fun createStructureViewModel(editor: Editor?) = TwigStructureViewModel(psiFile, editor)
                }

                val view = builder.createStructureView(fileEditor, psiFile.project)
                return StructureViewComposite.StructureViewDescriptor(TwigLanguage.INSTANCE.displayName, view, TwigFileType.INSTANCE.icon)
            }

        }
    }
}

package com.fisk.twig.structure

import com.intellij.ide.structureView.impl.StructureViewComposite
import com.intellij.ide.structureView.newStructureView.StructureViewComponent
import com.intellij.lang.LanguageStructureViewBuilder
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.util.Disposer
import com.intellij.psi.PsiFile
import com.intellij.testFramework.PlatformTestUtil.assertTreeEqual
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import com.intellij.util.Consumer

class TwigStructureViewTest : LightPlatformCodeInsightFixtureTestCase() {

    private fun doStructureViewTest(fileText: String, expectedTree: String) {
        myFixture.configureByText(ourTestFileName, fileText)

        testStructureView(myFixture.file, Consumer { component ->
            val tree = (component.selectedStructureView as StructureViewComponent).tree

            // expand the whole tree
            val rowCount = tree.rowCount
            for (i in 0..rowCount) {
                tree.expandRow(i)
            }

            assertTreeEqual(tree, expectedTree + "\n")
        })
    }

    fun testStructureView(file: PsiFile, consumer: Consumer<StructureViewComposite>) {
        val vFile = file.virtualFile
        val fileEditor = FileEditorManager.getInstance(project).getSelectedEditor(vFile)
        val builder = LanguageStructureViewBuilder.INSTANCE.getStructureViewBuilder(file)!!

        var composite: StructureViewComposite? = null
        try {
            composite = builder.createStructureView(fileEditor, file.project) as StructureViewComposite
            consumer.consume(composite)
        } finally {
            if (composite != null) Disposer.dispose(composite)
        }
    }

    fun testNestedBlocks() {
        doStructureViewTest(

                "{% if foo %}\n" +
                        "    {% block bar %}\n" +
                        "        {{ baz }}<caret>\n" +
                        "    {% endblock %}\n" +
                        "{% endif %}\n",

                "-" + ourTestFileName + "\n" +
                        " -if foo\n" +
                        "  block bar"
        )
    }

    fun testUnclosedInverse() {
        doStructureViewTest(

                "{% if foo %}\n" + "{% else %}\n" + "{% endif %}",

                "-" + ourTestFileName + "\n" +
                        " if foo"
        )
    }

    fun testAllConstructs() {
        doStructureViewTest(

                "{% if foo %}\n" +
                        "{{ expr }}\n" +
                        "{% endif %}\n",

                "-" + ourTestFileName + "\n" +
                        " if foo"
        )
    }

    companion object {
        private val ourTestFileName = "test.twig"
    }
}

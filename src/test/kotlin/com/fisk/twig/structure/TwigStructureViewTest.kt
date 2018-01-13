package com.fisk.twig.structure

import com.intellij.ide.structureView.impl.StructureViewComposite
import com.intellij.ide.structureView.newStructureView.StructureViewComponent
import com.intellij.lang.LanguageStructureViewBuilder
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.util.Disposer
import com.intellij.psi.PsiFile
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.testFramework.PlatformTestUtil.assertTreeEqual
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import com.intellij.util.Consumer
import com.intellij.util.ui.tree.TreeUtil


class TwigStructureViewTest : LightPlatformCodeInsightFixtureTestCase() {
    companion object {
        private val ourTestFileName = "test.twig"
    }

    private fun doStructureViewTest(fileText: String, expectedTree: String) {
        myFixture.configureByText(ourTestFileName, fileText)

        doTestStructureView(myFixture.file, Consumer { composite ->
            val svc = composite.selectedStructureView as StructureViewComponent
            PlatformTestUtil.waitForPromise(svc.rebuildAndUpdate())
            TreeUtil.expandAll(svc.tree)
            assertTreeEqual(svc.tree, expectedTree + "\n")
        })
    }

    private fun doTestStructureView(file: PsiFile, consumer: Consumer<StructureViewComposite>) {
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

    /**
     * This also tests with various tags and whitespaces to
     * make sure tag content is extracted correctly
     */
    fun testNestedBlocks() {
        doStructureViewTest(

                "{%-if foo-%}\n" +
                        "    {%block bar%}\n" +
                        "        {{- baz -}}<caret>\n" +
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
}

package com.fisk.twig.indices

import com.fisk.twig.file.TwigFileType
import com.fisk.twig.psi.TwigPsiFile
import com.fisk.twig.util.TwigFileUtil
import com.intellij.util.indexing.*
import com.intellij.util.io.VoidDataExternalizer
import gnu.trove.THashMap
import com.intellij.util.io.EnumeratorStringDescriptor

class TwigVariableIndex : FileBasedIndexExtension<String, Void>() {
    val KEY: ID<String, Void> = ID.create("com.fisk.twig.indices.variables")
    private val descriptor = EnumeratorStringDescriptor()

    override fun getKeyDescriptor() = descriptor

    override fun getInputFilter() = FileBasedIndex.InputFilter {
        it.fileType == TwigFileType.INSTANCE
    }

    override fun getValueExternalizer(): VoidDataExternalizer = VoidDataExternalizer.INSTANCE

    override fun getVersion() = 1

    override fun getName() = KEY

    override fun dependsOnFileContent() = true

    override fun getIndexer(): DataIndexer<String, Void, FileContent> {
        return DataIndexer {
            val map = THashMap<String, Void>()

            if (it.psiFile is TwigPsiFile) {
                // todo: populate map
                TwigFileUtil.getParents(it.psiFile)
            }

            map
        }
    }
}
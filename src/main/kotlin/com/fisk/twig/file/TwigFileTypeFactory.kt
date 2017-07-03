package com.fisk.twig.file

import com.intellij.openapi.fileTypes.FileTypeConsumer
import com.intellij.openapi.fileTypes.FileTypeFactory

class TwigFileTypeFactory : FileTypeFactory() {
    override fun createFileTypes(consumer: FileTypeConsumer) {
        consumer.consume(TwigFileType.INSTANCE, TwigFileType.DEFAULT_EXTENSION)
    }
}

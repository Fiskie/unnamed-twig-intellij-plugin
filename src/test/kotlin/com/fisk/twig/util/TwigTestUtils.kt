package com.fisk.twig.util

import com.fisk.twig.config.TwigConfig
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import org.jetbrains.annotations.TestOnly

import java.io.File

object TwigTestUtils {
    /**
     * The root of the test data directory
     */
    val BASE_TEST_DATA_PATH = findTestDataPath()

    private fun findTestDataPath(): String {
        val f = File("src/test", "resources")

        if (f.exists()) {
            return f.absolutePath
        }

        return PathManager.getHomePath() + "/contrib/twig/src/test/resources"
    }
}

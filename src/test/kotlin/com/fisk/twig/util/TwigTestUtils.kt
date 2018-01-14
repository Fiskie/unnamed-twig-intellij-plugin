package com.fisk.twig.util

import com.intellij.openapi.application.PathManager
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

package io.github.sgrishchenko.karakum.extension

import io.github.sgrishchenko.karakum.configuration.PartialConfiguration
import io.github.sgrishchenko.karakum.generateTests
import js.objects.recordOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class ExtensionTest {
    @Test
    fun test() = runTest {
        generateTests("extension") { output ->
            recordOf(
                "input" to "**/*.d.ts",
                "output" to output,
                "libraryName" to "extension",
                "verbose" to true,
                "extensions" to "./extensions.js",
            ).unsafeCast<PartialConfiguration>()
        }
    }
}

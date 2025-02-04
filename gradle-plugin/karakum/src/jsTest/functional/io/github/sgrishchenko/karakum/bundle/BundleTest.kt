package io.github.sgrishchenko.karakum.bundle

import io.github.sgrishchenko.karakum.configuration.PartialConfiguration
import io.github.sgrishchenko.karakum.generateTests
import js.objects.recordOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class BundleTest {
    @Test
    fun test() = runTest {
        generateTests("bundle") { output ->
            recordOf(
                "input" to "**/*.d.ts",
                "output" to "${output}/customBundle.kt",
                "libraryName" to "sandbox-bundle",
                "granularity" to "bundle",
                "verbose" to true,
            ).unsafeCast<PartialConfiguration>()
        }
    }
}

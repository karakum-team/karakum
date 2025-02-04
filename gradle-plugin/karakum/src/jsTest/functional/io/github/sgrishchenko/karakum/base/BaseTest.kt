package io.github.sgrishchenko.karakum.base

import io.github.sgrishchenko.karakum.configuration.PartialConfiguration
import io.github.sgrishchenko.karakum.generateTests
import js.objects.recordOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class BaseTest {
    @Test
    fun test() = runTest {
        generateTests("base") { output ->
            recordOf(
                "input" to "**/*.d.ts",
                "output" to output,
                "libraryName" to "sandbox-base",
                "verbose" to true,
            ).unsafeCast<PartialConfiguration>()
        }
    }
}

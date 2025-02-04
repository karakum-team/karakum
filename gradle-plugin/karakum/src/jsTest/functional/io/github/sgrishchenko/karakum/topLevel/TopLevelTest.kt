package io.github.sgrishchenko.karakum.topLevel

import io.github.sgrishchenko.karakum.configuration.PartialConfiguration
import io.github.sgrishchenko.karakum.generateTests
import js.objects.recordOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class TopLevelTest {
    @Test
    fun test() = runTest {
        generateTests("topLevel") { output ->
            recordOf(
                "input" to "**/*.d.ts",
                "output" to output,
                "libraryName" to "sandbox-top-level",
                "granularity" to "top-level",
                "moduleNameMapper" to recordOf(
                    "^(?!sandbox-top-level/myFunction1$).*" to "sandbox-top-level"
                ),
                "verbose" to true,
            ).unsafeCast<PartialConfiguration>()
        }
    }
}

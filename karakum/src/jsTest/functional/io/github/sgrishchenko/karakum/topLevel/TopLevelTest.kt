package io.github.sgrishchenko.karakum.topLevel

import io.github.sgrishchenko.karakum.configuration.Granularity
import io.github.sgrishchenko.karakum.generateTests
import io.github.sgrishchenko.karakum.topLevel.plugins.blankOutExportStatement
import io.github.sgrishchenko.karakum.util.manyOf
import js.objects.recordOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class TopLevelTest {
    @Test
    fun test() = runTest {
        generateTests("topLevel") { testOutput ->
            input = manyOf("**/*.d.ts")
            output = testOutput
            libraryName = "sandbox-top-level"
            granularity = Granularity.topLevel
            moduleNameMapper = recordOf(
                "^(?!sandbox-top-level/myFunction1$).*" to "sandbox-top-level"
            )
            verbose = true
            plugins = manyOf(
                blankOutExportStatement
            )
        }
    }
}

package io.github.sgrishchenko.karakum.topLevel

import io.github.sgrishchenko.karakum.generateTests
import io.github.sgrishchenko.karakum.topLevel.plugins.blankOutExportStatement
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class TopLevelTest {
    @Test
    fun test() = runTest {
        generateTests("topLevel") { testOutput ->
            input = listOf("**/*.d.ts")
            output = testOutput
            libraryName = "sandbox-top-level"
            moduleNameMapper = mapOf(
                "^(?!sandbox-top-level/myFunction1$).*" to "sandbox-top-level"
            )
            plugins = listOf(
                blankOutExportStatement
            )
        }
    }
}

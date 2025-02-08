package io.github.sgrishchenko.karakum.base

import io.github.sgrishchenko.karakum.generateTests
import io.github.sgrishchenko.karakum.util.manyOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class BaseTest {
    @Test
    fun test() = runTest {
        generateTests("base") { testOutput ->
            input = manyOf("**/*.d.ts")
            output = testOutput
            libraryName = "sandbox-base"
            verbose = true
        }
    }
}

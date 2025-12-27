package io.github.sgrishchenko.karakum.bundle

import io.github.sgrishchenko.karakum.configuration.Granularity
import io.github.sgrishchenko.karakum.configuration.bundle
import io.github.sgrishchenko.karakum.generateTests
import io.github.sgrishchenko.karakum.util.manyOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class BundleTest {
    @Test
    fun test() = runTest {
        generateTests("bundle") { testOutput ->
            input = manyOf("**/*.d.ts")
            output = "${testOutput}/customBundle.kt"
            libraryName = "sandbox-bundle"
            granularity = Granularity.bundle
        }
    }
}

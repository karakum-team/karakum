package io.github.sgrishchenko.karakum.extension

import io.github.sgrishchenko.karakum.extension.nameResolvers.convertErrorTypeReferenceNode
import io.github.sgrishchenko.karakum.extension.plugins.resolveUnionMemberDuplicateName
import io.github.sgrishchenko.karakum.generateTests
import io.github.sgrishchenko.karakum.util.manyOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class ExtensionTest {
    @Test
    fun test() = runTest {
        generateTests("extension") { testOutput ->
            input = manyOf("**/*.d.ts")
            output = testOutput
            libraryName = "extension"
            verbose = true
            plugins = manyOf(
                convertErrorTypeReferenceNode
            )
            nameResolvers = manyOf(
                resolveUnionMemberDuplicateName
            )
        }
    }
}

package io.github.sgrishchenko.karakum.base

import io.github.sgrishchenko.karakum.base.inheritanceModifiers.modifyClassInheritance
import io.github.sgrishchenko.karakum.base.inheritanceModifiers.modifyPropertyInheritance
import io.github.sgrishchenko.karakum.generateTests
import io.github.sgrishchenko.karakum.util.manyOf
import js.objects.recordOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class BaseTest {
    @Test
    fun test() = runTest {
        generateTests("base") { testOutput ->
            input = manyOf("**/*.d.ts")
            output = testOutput
            libraryName = "sandbox-base"
            inheritanceModifiers = manyOf(
                modifyClassInheritance,
                modifyPropertyInheritance,
            )
            importInjector = recordOf(
                "mappedType/simple.kt" to arrayOf(
                    "js.promise.Promise",
                ),
                "property/doubleOptionality.kt" to arrayOf(
                    "sandbox.base.AgnosticBaseRouteObjectHandle15",
                ),
                "void/simple.kt" to arrayOf(
                    "js.promise.Promise",
                ),
            )
        }
    }
}

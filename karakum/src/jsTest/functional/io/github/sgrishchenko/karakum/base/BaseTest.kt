package io.github.sgrishchenko.karakum.base

import io.github.sgrishchenko.karakum.base.inheritanceModifiers.modifyClassInheritance
import io.github.sgrishchenko.karakum.base.inheritanceModifiers.modifyPropertyInheritance
import io.github.sgrishchenko.karakum.generateTests
import js.objects.unsafeJso
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class BaseTest {
    @Test
    fun test() = runTest {
        generateTests("base") { testOutput ->
            input = listOf("**/*.d.ts")
            output = testOutput
            libraryName = "sandbox-base"
            inheritanceModifiers = listOf(
                modifyClassInheritance,
                modifyPropertyInheritance,
            )
            packageNameMapper = mapOf(
                "VariableWithAnonymousType.kt" to "VariableWithAnonymousType.interface.kt",
            )
            importInjector = mapOf(
                "importType/simple/DataRouterStateContext.kt" to listOf(
                    "sandbox.base.importType.router.RouterState"
                ),
                "property/doubleOptionality/AgnosticBaseRouteObject.kt" to listOf(
                    "sandbox.base.generated.AgnosticBaseRouteObjectHandle15",
                ),
            )
            compilerOptions = unsafeJso {
                lib = arrayOf(
                    "lib.esnext.d.ts",
                    "lib.dom.d.ts",
                )
            }
        }
    }
}

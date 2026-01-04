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
            packageNameMapper = recordOf(
                "VariableWithAnonymousType.kt" to "VariableWithAnonymousType.interface.kt"
            )
            importInjector = recordOf(
                "importType/simple/DataRouterStateContext.kt" to arrayOf(
                    "sandbox.base.importType.router.RouterState"
                ),
                "mappedType/simple/MappedTypeContainerMappedType.kt" to arrayOf(
                    "js.promise.Promise",
                ),
                "mappedType/simple/NamedOptionsFlags.kt" to arrayOf(
                    "js.promise.Promise",
                ),
                "mappedType/simple/OptionalOptionsFlags.kt" to arrayOf(
                    "js.promise.Promise",
                ),
                "mappedType/simple/OptionsFlags.kt" to arrayOf(
                    "js.promise.Promise",
                ),
                "mappedType/simple/OptionsFlagsWithTypeLiteral.kt" to arrayOf(
                    "js.promise.Promise",
                ),
                "mappedType/simple/ReadonlyOptionsFlags.kt" to arrayOf(
                    "js.promise.Promise",
                ),
                "property/doubleOptionality/AgnosticBaseRouteObject.kt" to arrayOf(
                    "sandbox.base.generated.AgnosticBaseRouteObjectHandle15",
                ),
                "void/simple/functionWithPromiseVoidReturnType.kt" to arrayOf(
                    "js.promise.Promise",
                ),
            )
        }
    }
}

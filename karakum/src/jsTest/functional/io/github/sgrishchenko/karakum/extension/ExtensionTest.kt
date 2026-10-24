package io.github.sgrishchenko.karakum.extension

import io.github.sgrishchenko.karakum.extension.annotations.configurable.JsPlainObjectAnnotation
import io.github.sgrishchenko.karakum.extension.mutabilityModifiers.modifyCustomMutability
import io.github.sgrishchenko.karakum.extension.plugins.convertErrorTypeReferenceNode
import io.github.sgrishchenko.karakum.extension.plugins.configurable.NumberPlugin
import io.github.sgrishchenko.karakum.extension.plugins.configurable.NumberPluginStrategy
import io.github.sgrishchenko.karakum.extension.plugins.configurable.PromiseFunctionPlugin
import io.github.sgrishchenko.karakum.extension.plugins.configurable.PromiseMethodPlugin
import io.github.sgrishchenko.karakum.extension.plugins.configurable.PromiseResultPlugin
import io.github.sgrishchenko.karakum.extension.plugins.configurable.loose
import io.github.sgrishchenko.karakum.extension.nameResolvers.resolveUnionMemberDuplicateName
import io.github.sgrishchenko.karakum.generateTests
import js.reflect.invoke
import kotlinx.coroutines.test.runTest
import typescript.Node
import typescript.isClassDeclaration
import typescript.isFunctionDeclaration
import typescript.isIdentifier
import typescript.isInterfaceDeclaration
import typescript.isMethodDeclaration
import typescript.isMethodSignature
import typescript.isParameter
import typescript.isPropertyDeclaration
import typescript.isPropertySignature
import typescript.isTypeReferenceNode
import typescript.isUnionTypeNode
import kotlin.test.Test

private fun isCustomPromise(node: Node, context: Context): Boolean {
    if (!isTypeReferenceNode(node)) return false

    val typeName = node.typeName

    return isIdentifier(typeName) && typeName.text == "CustomPromise"
}

class ExtensionTest {
    @Test
    fun test() = runTest {
        generateTests("extension") { testOutput ->
            input = listOf("**/*.d.ts")
            output = testOutput
            libraryName = "extension"
            moduleNameMapper = mapOf(
                // prevent importing of fake modules
                "extension/.+" to ""
            )
            plugins = listOf(
                NumberPlugin(
                    NumberPluginStrategy.loose,
                    defaultNumberType = "Double /* fallback */",
                    "Int" to match {
                        match(isInterfaceDeclaration::invoke, withName("InterfaceWithNumbers")) {
                            match(isPropertySignature::invoke, withName("numberField1"))
                            match(isMethodSignature::invoke, withName("numberMethod1"))
                                .match(isParameter::invoke, withName("numberParam"))
                        }

                        match(isClassDeclaration::invoke, withName("ClassWithNumbers")) {
                            match(isPropertyDeclaration::invoke, withName("numberField1"))
                            match(isMethodDeclaration::invoke, withName("numberMethod1"))
                                .match(isParameter::invoke, withName("numberParam"))
                        }
                    },
                    "Double" to match {
                        match(isInterfaceDeclaration::invoke, withName("InterfaceWithNumbers")) {
                            match(isPropertySignature::invoke, withName("numberField2"))
                            match(isMethodSignature::invoke, withName("numberMethod2"))
                                .match(isParameter::invoke, withName("numberParam"))
                            match(isMethodSignature::invoke, withName("numberMethod3"))
                        }

                        match(isClassDeclaration::invoke, withName("ClassWithNumbers")) {
                            match(isPropertyDeclaration::invoke, withName("numberField2"))
                            match(isMethodDeclaration::invoke, withName("numberMethod2"))
                                .match(isParameter::invoke, withName("numberParam"))
                            match(isMethodDeclaration::invoke, withName("numberMethod3"))
                        }
                    },
                ),

                PromiseResultPlugin(
                    ignore = match {
                        match(isFunctionDeclaration::invoke, withName("returnsPromiseResultIgnored"))
                    }
                ),
                PromiseResultPlugin(
                    isPromiseType = match(::isCustomPromise),
                    renderPayload = { _, _, _ -> "Any?" }
                ),

                PromiseFunctionPlugin(
                    ignore = match {
                        match(isFunctionDeclaration::invoke, withName("returnsPromiseIgnored"))
                    },
                    exclude = match {
                        match(
                            isFunctionDeclaration::invoke,
                            withName("returnsPromise2"),
                            { _, context ->
                                context.signature.first().parameter.type?.let { isUnionTypeNode(it) } == true
                            }
                        )
                    }
                ),
                PromiseFunctionPlugin(
                    isPromiseType = match(::isCustomPromise),
                    renderPayload = { _, _, _ -> "Any?" }
                ),

                PromiseMethodPlugin(
                    ignore = match {
                        match(isInterfaceDeclaration::invoke, withName("InterfaceWithPromiseMethods"))
                            .match(isMethodSignature::invoke, withName("returnsPromiseIgnored"))

                        match(isClassDeclaration::invoke, withName("ClassWithPromiseMethods"))
                            .match(isMethodDeclaration::invoke, withName("returnsPromiseIgnored"))
                    },
                    exclude = match {
                        match(
                            isMethodSignature::invoke,
                            withName("returnsPromise2"),
                            { _, context ->
                                context.signature.first().parameter.type?.let { isUnionTypeNode(it) } == true
                            }
                        )
                        match(
                            isMethodDeclaration::invoke,
                            withName("returnsPromise2"),
                            { _, context ->
                                context.signature.first().parameter.type?.let { isUnionTypeNode(it) } == true
                            }
                        )
                    }
                ),
                PromiseMethodPlugin(
                    isPromiseType = match(::isCustomPromise),
                    renderPayload = { _, _, _ -> "Any?" }
                ),

                convertErrorTypeReferenceNode,
            )

            nameResolvers = listOf(
                ::resolveUnionMemberDuplicateName
            )

            mutabilityModifiers = listOf(
                ::modifyCustomMutability,
            )

            annotations = listOf(
                JsPlainObjectAnnotation(
                    ignore = match {
                        match(isInterfaceDeclaration::invoke, withName("MutableContainerClass"))
                    }
                )
            )
        }
    }
}

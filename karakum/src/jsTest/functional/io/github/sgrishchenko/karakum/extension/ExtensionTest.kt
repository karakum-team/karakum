package io.github.sgrishchenko.karakum.extension

import io.github.sgrishchenko.karakum.extension.nameResolvers.convertErrorTypeReferenceNode
import io.github.sgrishchenko.karakum.extension.plugins.configurable.NumberPlugin
import io.github.sgrishchenko.karakum.extension.plugins.configurable.NumberPluginStrategy
import io.github.sgrishchenko.karakum.extension.plugins.configurable.PromiseFunctionPlugin
import io.github.sgrishchenko.karakum.extension.plugins.configurable.PromiseMethodPlugin
import io.github.sgrishchenko.karakum.extension.plugins.configurable.PromiseResultPlugin
import io.github.sgrishchenko.karakum.extension.plugins.configurable.SignatureContext
import io.github.sgrishchenko.karakum.extension.plugins.configurable.loose
import io.github.sgrishchenko.karakum.extension.plugins.resolveUnionMemberDuplicateName
import io.github.sgrishchenko.karakum.generateTests
import io.github.sgrishchenko.karakum.util.manyOf
import js.objects.recordOf
import kotlinx.coroutines.test.runTest
import typescript.Node
import typescript.asArray
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
            input = manyOf("**/*.d.ts")
            output = testOutput
            libraryName = "extension"
            moduleNameMapper = recordOf(
                // prevent importing of fake modules
                "extension/.+" to ""
            )
            plugins = manyOf(
                NumberPlugin(
                    NumberPluginStrategy.loose,
                    defaultNumberType = "Double /* fallback */",
                    "Int" to match {
                        match(::isInterfaceDeclaration, withName("InterfaceWithNumbers")) {
                            match(::isPropertySignature, withName("numberField1"))
                            match(::isMethodSignature, withName("numberMethod1"))
                                .match(::isParameter, withName("numberParam"))
                        }

                        match(::isClassDeclaration, withName("ClassWithNumbers")) {
                            match(::isPropertyDeclaration, withName("numberField1"))
                            match(::isMethodDeclaration, withName("numberMethod1"))
                                .match(::isParameter, withName("numberParam"))
                        }
                    },
                    "Double" to match {
                        match(::isInterfaceDeclaration, withName("InterfaceWithNumbers")) {
                            match(::isPropertySignature, withName("numberField2"))
                            match(::isMethodSignature, withName("numberMethod2"))
                                .match(::isParameter, withName("numberParam"))
                            match(::isMethodSignature, withName("numberMethod3"))
                        }

                        match(::isClassDeclaration, withName("ClassWithNumbers")) {
                            match(::isPropertyDeclaration, withName("numberField2"))
                            match(::isMethodDeclaration, withName("numberMethod2"))
                                .match(::isParameter, withName("numberParam"))
                            match(::isMethodDeclaration, withName("numberMethod3"))
                        }
                    },
                ),

                PromiseResultPlugin(
                    ignore = match {
                        match(::isFunctionDeclaration, withName("returnsPromiseResultIgnored"))
                    }
                ),
                PromiseResultPlugin(
                    isPromiseType = match(::isCustomPromise),
                    renderPayload = { _, _, _ -> "Any?" }
                ),

                PromiseFunctionPlugin(
                    ignore = match {
                        match(::isFunctionDeclaration, withName("returnsPromiseIgnored"))
                    },
                    exclude = match {
                        match(
                            ::isFunctionDeclaration,
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
                        match(::isInterfaceDeclaration, withName("InterfaceWithPromiseMethods"))
                            .match(::isMethodSignature, withName("returnsPromiseIgnored"))

                        match(::isClassDeclaration, withName("ClassWithPromiseMethods"))
                            .match(::isMethodDeclaration, withName("returnsPromiseIgnored"))
                    },
                    exclude = match {
                        match(
                            ::isMethodSignature,
                            withName("returnsPromise2"),
                            { _, context ->
                                context.signature.first().parameter.type?.let { isUnionTypeNode(it) } == true
                            }
                        )
                        match(
                            ::isMethodDeclaration,
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
            nameResolvers = manyOf(
                resolveUnionMemberDuplicateName
            )
        }
    }
}

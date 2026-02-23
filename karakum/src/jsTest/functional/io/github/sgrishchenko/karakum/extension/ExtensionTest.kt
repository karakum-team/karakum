package io.github.sgrishchenko.karakum.extension

import io.github.sgrishchenko.karakum.extension.nameResolvers.convertErrorTypeReferenceNode
import io.github.sgrishchenko.karakum.extension.plugins.configurable.NumberPlugin
import io.github.sgrishchenko.karakum.extension.plugins.configurable.NumberPluginStrategy
import io.github.sgrishchenko.karakum.extension.plugins.configurable.PromiseFunctionPlugin
import io.github.sgrishchenko.karakum.extension.plugins.configurable.PromiseResultPlugin
import io.github.sgrishchenko.karakum.extension.plugins.configurable.loose
import io.github.sgrishchenko.karakum.extension.plugins.resolveUnionMemberDuplicateName
import io.github.sgrishchenko.karakum.generateTests
import io.github.sgrishchenko.karakum.util.manyOf
import js.objects.recordOf
import kotlinx.coroutines.test.runTest
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
                        match(::isInterfaceDeclaration, "InterfaceWithNumbers") {
                            match(::isPropertySignature, "numberField1")
                            match(::isMethodSignature, "numberMethod1")
                                .match(::isParameter, "numberParam")
                        }

                        match(::isClassDeclaration, "ClassWithNumbers") {
                            match(::isPropertyDeclaration, "numberField1")
                            match(::isMethodDeclaration, "numberMethod1")
                                .match(::isParameter, "numberParam")
                        }
                    },
                    "Double" to match {
                        match(::isInterfaceDeclaration, "InterfaceWithNumbers") {
                            match(::isPropertySignature, "numberField2")
                            match(::isMethodSignature, "numberMethod2")
                                .match(::isParameter, "numberParam")
                            match(::isMethodSignature, "numberMethod3")
                        }

                        match(::isClassDeclaration, "ClassWithNumbers") {
                            match(::isPropertyDeclaration, "numberField2")
                            match(::isMethodDeclaration, "numberMethod2")
                                .match(::isParameter, "numberParam")
                            match(::isMethodDeclaration, "numberMethod3")
                        }
                    },
                ),
                PromiseResultPlugin(
                    ignore = match {
                        match(::isFunctionDeclaration, "returnsPromiseResultIgnored")
                    }
                ),
                PromiseFunctionPlugin(
                    ignore = match {
                        match(::isFunctionDeclaration, "returnsPromiseIgnored")
                    },
                    exclude = { node, signature ->
                        node.name?.text == "returnsPromise2"
                                && node.parameters.asArray().first().type?.let { isUnionTypeNode(it) } == true
                    }
                ),
                PromiseFunctionPlugin(
                    isPromiseType = isPromiseType@{ node, _ ->
                        if (!isTypeReferenceNode(node)) return@isPromiseType false

                        val typeName = node.typeName

                        isIdentifier(typeName) && typeName.text == "CustomPromise"
                    },
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

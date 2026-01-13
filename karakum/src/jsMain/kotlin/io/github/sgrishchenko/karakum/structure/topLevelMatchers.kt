package io.github.sgrishchenko.karakum.structure

import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.configuration.`object`
import io.github.sgrishchenko.karakum.structure.import.ImportInfo
import io.github.sgrishchenko.karakum.structure.namespace.NamespaceInfo
import io.github.sgrishchenko.karakum.structure.namespace.extractNamespaceName
import js.array.ReadonlyArray
import kotlinx.js.JsPlainObject
import typescript.Declaration
import typescript.ModuleDeclaration
import typescript.Node
import typescript.asArray
import typescript.isClassDeclaration
import typescript.isEnumDeclaration
import typescript.isFunctionDeclaration
import typescript.isIdentifier
import typescript.isInterfaceDeclaration
import typescript.isModuleDeclaration
import typescript.isStringLiteral
import typescript.isTypeAliasDeclaration
import typescript.isVariableStatement

@JsPlainObject
external interface TopLevelMatch {
    val name: String
    val node: Declaration
    val hasRuntime: Boolean
    val imports: ReadonlyArray<String>?
}

typealias TopLevelMatcher = (node: Node) -> ReadonlyArray<TopLevelMatch>?

private val classMatcher: TopLevelMatcher = matcher@{ node ->
    if (isClassDeclaration(node)) {
        val name = node.name?.text ?: return@matcher null

        arrayOf(TopLevelMatch(
            name = name,
            node = node,
            hasRuntime = true,
        ))
    } else {
        null
    }
}

private val interfaceMatcher: TopLevelMatcher = { node ->
    if (isInterfaceDeclaration(node)) {
        val name = node.name.text

        arrayOf(TopLevelMatch(
            name = name,
            node = node,
            hasRuntime = false,
        ))
    } else {
        null
    }
}

private val typeAliasMatcher: TopLevelMatcher = { node ->
    if (isTypeAliasDeclaration(node)) {
        val name = node.name.text

        arrayOf(TopLevelMatch(
            name = name,
            node = node,
            hasRuntime = false,
        ))
    } else {
        null
    }
}

private val enumMatcher: TopLevelMatcher = { node ->
    if (isEnumDeclaration(node)) {
        val name = node.name.text

        arrayOf(TopLevelMatch(
            name = name,
            node = node,
            hasRuntime = true,
        ))
    } else {
        null
    }
}

private val functionMatcher: TopLevelMatcher = matcher@{ node ->
    if (isFunctionDeclaration(node)) {
        val name = node.name?.text ?: return@matcher null

        arrayOf(TopLevelMatch(
            name = name,
            node = node,
            hasRuntime = true,
        ))
    } else {
        null
    }
}

private val variableMatcher: TopLevelMatcher = { node ->
    if (isVariableStatement(node)) {
        node.declarationList.declarations.asArray()
            .mapNotNull { declaration ->
                val nameNode = declaration.name
                if (!isIdentifier(nameNode)) return@mapNotNull null

                TopLevelMatch(
                    name = nameNode.text,
                    node = declaration,
                    hasRuntime = true,
                )
            }
            .toTypedArray()
    } else {
        null
    }
}

private fun moduleNameToFileName(moduleName: String): String {
    return moduleName.replace("""[<>:"/\\|?*]""".toRegex(), "_")
}

private fun createNamespaceMatcher(importInfo: ImportInfo, namespaceInfo: NamespaceInfo): TopLevelMatcher = matcher@{ node ->
    if (isModuleDeclaration(node)) {
        val namespaceStrategy = resolveNamespaceStrategy(node, namespaceInfo)
        if (namespaceStrategy != NamespaceStrategy.`object`) return@matcher null

        val nameNode = node.name
        val name = when {
            isStringLiteral(nameNode) -> moduleNameToFileName(nameNode.text)
            isIdentifier(nameNode) -> nameNode.text
            else -> return@matcher null
        }

        arrayOf(TopLevelMatch(
            name = name,
            node = node,
            hasRuntime = true,
            imports = importInfo[node],
        ))
    } else {
        null
    }
}

private fun resolveNamespaceStrategy(node: ModuleDeclaration, namespaceInfo: NamespaceInfo): NamespaceStrategy? {
    val name = extractNamespaceName(node)
    val detailedName = name.joinToString(separator = ".") { it.detailedName }

    return namespaceInfo.find { it.name == detailedName }?.strategy
}

private fun createTopLevelMatchers(importInfo: ImportInfo, namespaceInfo: NamespaceInfo): ReadonlyArray<TopLevelMatcher> = arrayOf(
    classMatcher,
    interfaceMatcher,
    typeAliasMatcher,
    enumMatcher,
    functionMatcher,
    variableMatcher,
    createNamespaceMatcher(importInfo, namespaceInfo),
)

fun createTopLevelMatcher(importInfo: ImportInfo, namespaceInfo: NamespaceInfo): TopLevelMatcher {
    val topLevelMatchers = createTopLevelMatchers(importInfo, namespaceInfo)

    return matcher@{ node ->
        for (matcher in topLevelMatchers) {
            val result = matcher(node)
            if (result != null) return@matcher result
        }

        null
    }
}

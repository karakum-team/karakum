package io.github.sgrishchenko.karakum.structure.namespace

import io.github.sgrishchenko.karakum.configuration.Configuration
import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.structure.StructureItem
import io.github.sgrishchenko.karakum.structure.module.extractModuleName
import io.github.sgrishchenko.karakum.structure.module.moduleNameToPackage
import js.array.ReadonlyArray
import js.objects.JsPlainObject
import js.objects.Object
import typescript.*

@JsPlainObject
external interface NamespaceInfoItem : StructureItem {
    val name: String
    val strategy: NamespaceStrategy
}

@JsPlainObject
external interface NamespaceNameChunk {
    val detailedName: String
    val simpleName: String
    val `package`: ReadonlyArray<String>
    val isAmbient: Boolean
}

val defaultNamespaceStrategy = NamespaceStrategy.`object`

fun extractNamespaceName(
    namespace: ModuleDeclaration,
    suffix: ReadonlyArray<NamespaceNameChunk> = emptyArray()
): ReadonlyArray<NamespaceNameChunk> {
    val namespaceName = namespace.name
    var simpleName = ""

    if (isStringLiteral(namespaceName)) simpleName = namespaceName.text
    if (isIdentifier(namespaceName)) simpleName = namespaceName.text
    if (simpleName.isEmpty()) error("Unsupported namespace name")

    val detailedName =
        if (isIdentifier(namespaceName)) simpleName
        else "\"${simpleName}\""

    val packageChunks = moduleNameToPackage(simpleName).map { it.lowercase() }
    val isAmbient = isStringLiteral(namespace.name)

    val nameChunk = NamespaceNameChunk(
        detailedName = detailedName,
        simpleName = simpleName,
        `package` = packageChunks.toTypedArray(),
        isAmbient = isAmbient,
    )

    val name = arrayOf(nameChunk) + suffix
    val parent = namespace.parent

    return if (isModuleDeclaration(parent)) {
        extractNamespaceName(parent, name)
    } else if (isModuleBlock(parent)) {
        extractNamespaceName(parent.parent, name)
    } else {
        name
    }
}

fun createNamespaceInfoItem(
    namespace: ModuleDeclaration,
    sourceFileName: String,
    imports: ReadonlyArray<String>,
    configuration: Configuration,
): NamespaceInfoItem {
    val namespaceStrategy = configuration.namespaceStrategy

    val defaultModuleName = extractModuleName(sourceFileName, configuration)
    val name = extractNamespaceName(namespace)

    val detailedName = name.joinToString(separator = ".") { it.detailedName }
    val simpleName = name.joinToString(separator = ".") { it.simpleName }

    val fileName = "namespace.kt"
    val packageChunks = name.flatMap { it.`package`.toList() }
    val hasRuntime = true

    var moduleName = defaultModuleName
    var qualifier: String? = simpleName

    val (firstChunk, restChunks) = with(name) { first() to drop(1) }

    if (firstChunk.isAmbient) {
        moduleName = firstChunk.simpleName

        qualifier = if (restChunks.isNotEmpty()) {
            restChunks.joinToString(separator = ".") { it.simpleName }
        } else {
            null
        }
    }

    for ((pattern, strategy) in Object.entries(namespaceStrategy)) {
        val regexp = pattern.toRegex()

        if (regexp.containsMatchIn(detailedName) || regexp.containsMatchIn(simpleName)) {
            return NamespaceInfoItem(
                fileName = fileName,
                `package` = packageChunks.toTypedArray(),
                moduleName = moduleName,
                qualifier = qualifier,
                hasRuntime = hasRuntime,
                imports = imports,
                name = detailedName,
                strategy = strategy
            )
        }
    }

    return NamespaceInfoItem(
        fileName = fileName,
        `package` = packageChunks.toTypedArray(),
        moduleName = moduleName,
        qualifier = qualifier,
        hasRuntime = hasRuntime,
        imports = imports,
        name = detailedName,
        strategy = defaultNamespaceStrategy
    )
}

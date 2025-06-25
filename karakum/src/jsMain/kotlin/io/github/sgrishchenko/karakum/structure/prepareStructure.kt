package io.github.sgrishchenko.karakum.structure

import io.github.sgrishchenko.karakum.configuration.Configuration
import io.github.sgrishchenko.karakum.configuration.Granularity
import io.github.sgrishchenko.karakum.structure.bundle.createBundleInfoItem
import io.github.sgrishchenko.karakum.structure.module.applyModuleNameMapper
import io.github.sgrishchenko.karakum.structure.`package`.applyPackageNameMapper
import js.array.ReadonlyArray
import kotlinx.js.JsPlainObject
import typescript.*

@JsPlainObject
external interface TopLevelMatch {
    val name: String
    val node: Declaration
    val hasRuntime: Boolean
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

private val topLevelMatchers: ReadonlyArray<TopLevelMatcher> = arrayOf(
    classMatcher,
    interfaceMatcher,
    typeAliasMatcher,
    enumMatcher,
    functionMatcher,
    variableMatcher,
)

private val topLevelMatcher: TopLevelMatcher = matcher@{ node ->
    for (matcher in topLevelMatchers) {
        val result = matcher(node)
        if (result != null) return@matcher result
    }

    null
}

private fun applyGranularity(
    items: ReadonlyArray<InputStructureItem>,
    configuration: Configuration,
): ReadonlyArray<InputStructureItem> {
    val granularity = configuration.granularity

    if (granularity == Granularity.file) {
        return items
    }

    if (granularity == Granularity.bundle) {
        val bundleItem = createBundleInfoItem(configuration)

        return arrayOf(
            InputStructureItem(
                fileName = bundleItem.fileName,
                `package` = bundleItem.`package`,
                moduleName = bundleItem.moduleName,
                qualifier = bundleItem.qualifier,
                hasRuntime = bundleItem.hasRuntime,
                imports = bundleItem.imports,

                nodes = items.flatMap { it.nodes.asIterable() }.toTypedArray(),
                meta = InputStructureItemMeta(
                    type = "Bundle",
                    name = items.joinToString(separator = "") { "\n\t${it.meta.name} [${it.meta.type}]" }
                ),
            )
        )
    }

    if (granularity == Granularity.topLevel) {
        return items.flatMap { item ->
            val result = mutableListOf<InputStructureItem>()

            for (node in item.nodes) {
                val topLevelMatches = topLevelMatcher(node)

                if (topLevelMatches != null) {
                    for (topLevelMatch in topLevelMatches) {
                        result += InputStructureItem(
                            fileName = "${topLevelMatch.name}.kt",
                            `package` = item.`package`,
                            moduleName = item.moduleName,
                            qualifier = item.qualifier,
                            hasRuntime = topLevelMatch.hasRuntime,
                            imports = item.imports,

                            nodes = arrayOf(topLevelMatch.node),
                            meta = item.meta,
                        )
                    }
                } else {
                    result += InputStructureItem(
                        fileName = item.fileName,
                        `package` = item.`package`,
                        moduleName = item.moduleName,
                        qualifier = item.qualifier,
                        hasRuntime = item.hasRuntime,
                        imports = item.imports,

                        nodes = arrayOf(node),
                        meta = item.meta,
                    )
                }
            }

            result
        }.toTypedArray()
    }

    error("Unknown granularity type: $granularity")
}

private fun applyMappers(
    items: ReadonlyArray<InputStructureItem>,
    configuration: Configuration,
): ReadonlyArray<InputStructureItem> {
    return items.map { item ->
        val packageMappingResult = applyPackageNameMapper(
            item.`package`,
            item.fileName,
            configuration,
        )

        val moduleMappingResult = applyModuleNameMapper(
            item.moduleName,
            item.qualifier,
            configuration,
        )

        InputStructureItem(
            fileName = packageMappingResult.fileName,
            `package` = packageMappingResult.`package`,

            moduleName = moduleMappingResult.moduleName,
            qualifier = moduleMappingResult.qualifier,

            hasRuntime = item.hasRuntime,
            imports = item.imports,

            nodes = item.nodes,
            meta = item.meta,
        )
    }.toTypedArray()
}

fun prepareStructure(
    items: ReadonlyArray<InputStructureItem>,
    configuration: Configuration,
): ReadonlyArray<InputStructureItem> {
    val granularItems = applyGranularity(items, configuration)
    return applyMappers(granularItems, configuration)
}

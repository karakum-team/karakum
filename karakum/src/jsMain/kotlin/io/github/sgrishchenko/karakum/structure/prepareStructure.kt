package io.github.sgrishchenko.karakum.structure

import io.github.sgrishchenko.karakum.configuration.Configuration
import io.github.sgrishchenko.karakum.structure.module.applyModuleNameMapper
import io.github.sgrishchenko.karakum.structure.`package`.applyPackageNameMapper
import js.array.ReadonlyArray

private fun applyGranularity(
    items: ReadonlyArray<InputStructureItem>,
    topLevelMatcher: TopLevelMatcher,
): ReadonlyArray<InputStructureItem> {
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
    topLevelMatcher: TopLevelMatcher,
    configuration: Configuration,
): ReadonlyArray<InputStructureItem> {
    val granularItems = applyGranularity(items, topLevelMatcher)
    return applyMappers(granularItems, configuration)
}

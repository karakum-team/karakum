package io.github.sgrishchenko.karakum.structure.module

import io.github.sgrishchenko.karakum.configuration.Configuration
import io.github.sgrishchenko.karakum.util.applyMapper
import js.objects.JsPlainObject

@JsPlainObject
external interface ModuleMappingResult {
    val moduleName: String
    val qualifier: String?
}

fun applyModuleNameMapper(
    moduleName: String,
    qualifier: String?,
    configuration: Configuration,
): ModuleMappingResult {
    val moduleNameMapper = configuration.moduleNameMapper

    val fullName = listOf(moduleName, qualifier ?: "")
        .filter { it.isNotEmpty() }
        .joinToString(separator = "#")

    val mappedFullName = applyMapper(fullName, moduleNameMapper)

    val mappedModuleChunks = mappedFullName.split("#")
    val mappedModuleName = mappedModuleChunks.first()
    val mappedQualifier = mappedModuleChunks.getOrNull(1)

    return ModuleMappingResult(
        moduleName = mappedModuleName,
        qualifier = mappedQualifier,
    )
}

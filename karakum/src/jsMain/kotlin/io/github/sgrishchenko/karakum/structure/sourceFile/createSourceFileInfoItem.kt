package io.github.sgrishchenko.karakum.structure.sourceFile

import io.github.sgrishchenko.karakum.configuration.Configuration
import io.github.sgrishchenko.karakum.structure.StructureItem
import io.github.sgrishchenko.karakum.structure.module.extractModuleName
import io.github.sgrishchenko.karakum.structure.module.moduleNameToPackage
import io.github.sgrishchenko.karakum.structure.`package`.dirNameToPackage
import io.github.sgrishchenko.karakum.structure.prepareLibraryName
import io.github.sgrishchenko.karakum.structure.removePrefix
import io.github.sgrishchenko.karakum.util.camelize
import js.array.ReadonlyArray
import kotlinx.js.JsPlainObject
import node.path.path

@JsPlainObject
external interface SourceFileInfoItem : StructureItem

private fun extractDirName(prefixes: ReadonlyArray<String>, sourceFileName: String): String {
    val relativeFileName = removePrefix(sourceFileName, prefixes)

    val dirName = path.dirname(relativeFileName)

    if (dirName == ".") {
        return ""
    }

    return dirName
}

private fun extractFileName(sourceFileName: String): String {
    return camelize(
        path.basename(sourceFileName)
            .replace("\\.d\\.ts$".toRegex(), "")
            .replace("\\.ts$".toRegex(), "")
    )
}

fun createSourceFileInfoItem(
    sourceFileName: String,
    imports: ReadonlyArray<String>,
    configuration: Configuration,
): SourceFileInfoItem {
    val inputRoots = configuration.inputRoots
    val libraryName = prepareLibraryName(configuration.libraryName)

    val dirName = extractDirName(inputRoots, sourceFileName)
    val fileName = extractFileName(sourceFileName)

    var packageChunks = moduleNameToPackage(libraryName) + dirNameToPackage(dirName)
    if (configuration.isolatedOutputPackage) packageChunks += fileName

    val moduleName = extractModuleName(sourceFileName, configuration)

    return SourceFileInfoItem(
        fileName = if (configuration.isolatedOutputPackage) {
            "module.kt"
        } else {
            "$fileName.kt"
        },
        `package` = packageChunks,
        moduleName = moduleName,
        qualifier = null,
        hasRuntime = true,
        imports = imports,
    )
}

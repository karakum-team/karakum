package io.github.sgrishchenko.karakum.structure.`package`

import io.github.sgrishchenko.karakum.configuration.Configuration
import io.github.sgrishchenko.karakum.structure.module.moduleNameToPackage
import io.github.sgrishchenko.karakum.structure.removePrefix
import js.array.ReadonlyArray
import node.path.path

fun dirNameToPackage(dirName: String): ReadonlyArray<String> {
    return dirName
        .split(path.posix.sep)
        .filter { it.isNotEmpty() }
        .toTypedArray()
}

fun packageToFileName(
    packageChunks: ReadonlyArray<String>,
    fileName: String,
): String {
    return path.posix.join(
        *packageChunks,
        fileName,
    )
}

fun packageToOutputFileName(
    packageChunks: ReadonlyArray<String>,
    fileName: String,
    configuration: Configuration,
): String {
    val libraryName = configuration.libraryName
    val libraryNameOutputPrefix = configuration.libraryNameOutputPrefix

    val result = packageToFileName(packageChunks, fileName)

    if (libraryNameOutputPrefix) {
        return result
    }

    val basePackage = path.posix.join(paths = moduleNameToPackage(libraryName)) + path.posix.sep

    return removePrefix(result, arrayOf(basePackage))
}

package io.github.sgrishchenko.karakum.structure.module

import io.github.sgrishchenko.karakum.configuration.Configuration
import io.github.sgrishchenko.karakum.structure.removePrefix
import js.array.ReadonlyArray
import node.path.path

private fun generateRelativeFileName(prefixes: ReadonlyArray<String>, sourceFileName: String): String {
    return removePrefix(sourceFileName, prefixes)
        .replace("\\.d\\.ts$".toRegex(), "")
        .replace("\\.ts$".toRegex(), "")
}

fun extractModuleName(
    sourceFileName: String,
    configuration: Configuration,
): String {
    val inputRoots = configuration.inputRoots
    val libraryName = configuration.libraryName

    val relativeFileName = generateRelativeFileName(inputRoots, sourceFileName)

    return path.posix.join(libraryName, relativeFileName)
}

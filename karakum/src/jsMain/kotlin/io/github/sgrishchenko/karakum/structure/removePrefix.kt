package io.github.sgrishchenko.karakum.structure

import js.array.ReadonlyArray

fun removePrefix(sourceFileName: String, prefixes: ReadonlyArray<String>): String {
    for (prefix in prefixes) {
        if (sourceFileName.startsWith(prefix)) {
            return sourceFileName.replace(prefix, "")
        }
    }

    return sourceFileName
}

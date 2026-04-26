package io.github.sgrishchenko.karakum.structure

fun removePrefix(sourceFileName: String, prefixes: List<String>): String {
    for (prefix in prefixes) {
        if (sourceFileName.startsWith(prefix)) {
            return sourceFileName.replace(prefix, "")
        }
    }

    return sourceFileName
}

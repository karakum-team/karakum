package io.github.sgrishchenko.karakum.util

import js.array.ReadonlyArray
import js.array.component1
import js.array.component2
import js.objects.Object
import js.objects.ReadonlyRecord

fun commonPrefix(vararg sources: ReadonlyArray<String>): ReadonlyArray<String> {
    var common = sources.firstOrNull() ?: emptyArray()

    for (i in 1..<sources.size) {
        val current = sources[i]
        val nextCommon = mutableListOf<String>()

        for (j in common.indices) {
            if (common[j] != current[j]) {
                break
            } else {
                nextCommon += common[j]
            }
        }

        common = nextCommon.toTypedArray()
    }

    return common
}

fun applyMapper(sourceFileName: String, mapper: ReadonlyRecord<String, String>): String {
    var currentFileName = sourceFileName

    for ((pattern, result) in Object.entries(mapper)) {
        val regexp = pattern.toRegex()

        if (regexp.containsMatchIn(currentFileName)) {
            currentFileName = currentFileName.replace(regexp, result)
        }
    }

    return currentFileName
}

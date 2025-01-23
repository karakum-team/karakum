package io.github.sgrishchenko.karakum.structure.`package`

import io.github.sgrishchenko.karakum.util.escapeIdentifier
import js.array.ReadonlyArray

fun createPackageName(packageChunks: ReadonlyArray<String>): String {
    return packageChunks.joinToString(separator = ".", transform = ::escapeIdentifier)
}

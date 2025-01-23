package io.github.sgrishchenko.karakum.structure.import

import js.array.ReadonlyArray

private fun removeComments(body: String): String {
    return body
        .replace("//.*".toRegex(), "")
        .replace("/\\*(.|\n)*?\\*/".toRegex(RegexOption.MULTILINE), "")
}

fun removeUnusedImports(imports: ReadonlyArray<String>, body: String): ReadonlyArray<String> {
    val cleanedBody = removeComments(body)

    return imports.filter { importItem ->
        if ("unhandled import" in importItem) return@filter true

        val matcher = if (" as " in importItem) {
            ".+ as (.+)$".toRegex()
        } else {
            ".+\\.(.+)$".toRegex()
        }

        val match = matcher.find(importItem) ?: error("Incorrect import: $importItem")

        val (_, name) = match.groupValues

        "\\b${name}\\b".toRegex().containsMatchIn(cleanedBody)
    }.toTypedArray()
}

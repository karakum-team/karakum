package io.github.sgrishchenko.karakum.util

import js.array.JsArray
import js.array.ReadonlyArray
import js.objects.jso
import node.fs.GlobOptionsWithFileTypes
import node.path.path

suspend fun glob(
    patterns: ReadonlyArray<String>,
    cwd: String,
    ignore: ReadonlyArray<String> = emptyArray()
): ReadonlyArray<String> {
    val fileNames = JsArray.fromAsync(
        node.fs.glob(patterns, jso<GlobOptionsWithFileTypes> {
            this.cwd = cwd
            withFileTypes = true
            excludeWithFileTypes = { file ->
                val fileName = path.resolve(file.parentPath, file.name)

                ignore.any { path.matchesGlob(fileName, it) }
            }
        })
    ).await()

    return fileNames
        .map { toPosix(path.resolve(it.parentPath, it.name)) }
        .toTypedArray()
}

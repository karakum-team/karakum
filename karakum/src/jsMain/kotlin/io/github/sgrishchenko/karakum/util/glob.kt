package io.github.sgrishchenko.karakum.util

import js.array.JsArrays
import js.promise.await
import node.fs.Dirent
import node.fs.GlobOptionsWithFileTypes
import node.path.path

suspend fun glob(
    patterns: List<String>,
    cwd: String,
    ignore: List<String> = emptyList()
): List<String> {
    val fileNames = JsArrays.fromAsync(
        node.fs.glob(patterns.toTypedArray(), GlobOptionsWithFileTypes(
            cwd = cwd,
            requiredWithFileTypes = true,
            exclude = { file: Dirent<String> ->
                val fileName = path.resolve(file.parentPath, file.name)

                ignore.any { path.matchesGlob(fileName, it) }
            },
        ))
    ).await()

    return fileNames
        .map { it.unsafeCast<Dirent<String>>() }
        .map { toPosix(path.resolve(it.parentPath, it.name)) }
}

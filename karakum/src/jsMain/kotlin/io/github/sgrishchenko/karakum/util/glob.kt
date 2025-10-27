package io.github.sgrishchenko.karakum.util

import js.array.JsArrays
import js.array.ReadonlyArray
import js.objects.unsafeJso
import js.promise.await
import node.fs.Dirent
import node.fs.GlobOptionsWithFileTypes
import node.path.path

suspend fun glob(
    patterns: ReadonlyArray<String>,
    cwd: String,
    ignore: ReadonlyArray<String> = emptyArray()
): ReadonlyArray<String> {
    val fileNames = JsArrays.fromAsync(
        node.fs.glob(patterns, unsafeJso<GlobOptionsWithFileTypes> {
            this.cwd = cwd
            withFileTypes = true
            exclude = { file: Dirent<String> ->
                val fileName = path.resolve(file.parentPath, file.name)

                ignore.any { path.matchesGlob(fileName, it) }
            }
        })
    ).await()

    return fileNames
        .map { it.unsafeCast<Dirent<String>>() }
        .map { toPosix(path.resolve(it.parentPath, it.name)) }
        .toTypedArray()
}

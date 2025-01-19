package io.github.sgrishchenko.karakum.structure.module

import js.array.ReadonlyArray

fun moduleNameToPackage(moduleName: String): ReadonlyArray<String> {
    // delimiters
    // "-" - react-router
    // ":" - node:url
    // "." - socket.io
    // "/" - @remix-run/router

    return moduleName
        .split("[-:./]".toRegex())
        .map { it.replace("\\W".toRegex(), "") }
        .filter { it.isNotEmpty() }
        .toTypedArray()
}

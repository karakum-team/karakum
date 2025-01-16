package io.github.sgrishchenko.karakum.util

import node.path.path
import node.url.pathToFileURL

fun toPosix(input: String): String {
    return if (path.sep == path.win32.sep) {
        input.replace(path.win32.sep, path.posix.sep)
    } else {
        input
    }
}

fun toAbsolute(input: String, cwd: String): String {
    val absolutePath = if (path.isAbsolute(input)) {
        input
    } else {
        path.join(cwd, input)
    }

    return toPosix(absolutePath)
}

fun toModuleName(input: String): String {
    return if (path.sep == path.win32.sep) {
        pathToFileURL(path.normalize(input)).toString()
    } else {
        input
    }
}

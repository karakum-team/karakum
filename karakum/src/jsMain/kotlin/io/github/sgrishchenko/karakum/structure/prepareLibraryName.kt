package io.github.sgrishchenko.karakum.structure

fun prepareLibraryName(libraryName: String): String {
    return libraryName
        .removePrefix("@types/")
        .let {
            if ("__" in it) {
                "@" + it.replace("__", "/")
            } else (
                it
            )
        }
}

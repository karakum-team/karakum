package io.github.sgrishchenko.karakum.structure

fun prepareLibraryName(libraryName: String): String {
    return if (libraryName.startsWith("@types/")) {
        libraryName
            .removePrefix("@types/")
            .let {
                if ("__" in it) {
                    "@" + it.replaceFirst("__", "/")
                } else {
                    it
                }
            }
    } else {
        libraryName
    }
}

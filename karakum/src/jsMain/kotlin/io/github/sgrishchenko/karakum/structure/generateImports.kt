package io.github.sgrishchenko.karakum.structure

import io.github.sgrishchenko.karakum.configuration.Configuration

fun generateImports(
    outputFileName: String,
    configuration: Configuration,
): String {
    val importInjector = configuration.importInjector

    val importSources = mutableListOf<String>()

    for ((pattern, imports) in importInjector) {
        val regexp = pattern.toRegex()

        if (regexp.containsMatchIn(outputFileName)) {
            importSources += imports
        }
    }

    return importSources.joinToString(separator = "\n") { "import $it" }
}

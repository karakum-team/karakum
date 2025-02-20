package io.github.sgrishchenko.karakum.structure

import io.github.sgrishchenko.karakum.configuration.Configuration
import io.github.sgrishchenko.karakum.structure.import.removeUnusedImports
import io.github.sgrishchenko.karakum.structure.`package`.createPackageName
import io.github.sgrishchenko.karakum.structure.`package`.packageToOutputFileName
import js.array.ReadonlyArray

fun createGeneratedFile(
    packageChunks: ReadonlyArray<String>,
    fileName: String,
    imports: ReadonlyArray<String>,
    body: String,
    configuration: Configuration,
): String {
    val packageName = createPackageName(packageChunks)

    val outputFileName = packageToOutputFileName(packageChunks, fileName, configuration)

    val resultImports = (
            removeUnusedImports(imports, body) +
                    generateImports(outputFileName, configuration)
            )
        .filter { it.isNotEmpty() }
        .joinToString(separator = "\n")

    val disclaimer = configuration.disclaimer

    val content = arrayOf(
        disclaimer,
        "package $packageName",
        resultImports,
        body,
    )
        .filter { it.isNotEmpty() }
        .joinToString(separator = "\n\n")

    return content.trim() + "\n"
}

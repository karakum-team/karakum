package io.github.sgrishchenko.karakum.structure

import io.github.sgrishchenko.karakum.configuration.Configuration
import io.github.sgrishchenko.karakum.structure.import.removeUnusedImports
import io.github.sgrishchenko.karakum.structure.`package`.createPackageName
import io.github.sgrishchenko.karakum.structure.`package`.packageToOutputFileName

fun createTargetFile(
    item: StructureItem,
    body: String,
    configuration: Configuration,
): String {
    val fileName = item.fileName
    val packageChunks = item.`package`
    val moduleName = item.moduleName
    val qualifier = item.qualifier
    val hasRuntime = item.hasRuntime
    val imports = item.imports

    val disclaimer = configuration.disclaimer

    val packageName = createPackageName(packageChunks)

    val outputFileName = packageToOutputFileName(packageChunks, fileName, configuration)

    val resultImports = (
            removeUnusedImports(imports, body) +
                    generateImports(outputFileName, configuration)
            )
        .filter { it.isNotEmpty() }
        .joinToString(separator = "\n")

    val jsModule = if (hasRuntime && moduleName.isNotEmpty()) "@file:JsModule(\"${moduleName}\")" else ""
    val jsQualifier = if (hasRuntime && qualifier != null) "@file:JsQualifier(\"${qualifier}\")" else ""

    val fileAnnotations = arrayOf(
        jsModule,
        jsQualifier,
    )
        .filter { it.isNotEmpty() }
        .joinToString(separator = "\n")

    val content = arrayOf(
        disclaimer,
        fileAnnotations,
        "package $packageName",
        resultImports,
        body,
    )
        .filter { it.isNotEmpty() }
        .joinToString(separator = "\n\n")

    return content.trim() + "\n"
}

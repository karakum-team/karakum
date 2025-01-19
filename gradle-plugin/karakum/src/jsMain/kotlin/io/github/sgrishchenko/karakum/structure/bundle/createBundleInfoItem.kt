package io.github.sgrishchenko.karakum.structure.bundle

import io.github.sgrishchenko.karakum.configuration.Configuration
import io.github.sgrishchenko.karakum.structure.StructureItem
import io.github.sgrishchenko.karakum.structure.sourceFile.createSourceFileInfoItem
import node.path.path

fun createBundleInfoItem(
    configuration: Configuration,
): StructureItem {
    val outputFileName = configuration.outputFileName

    val fileName = if (outputFileName != null) path.basename(outputFileName) else "bundle.kt"

    val syntheticItem = createSourceFileInfoItem("", emptyArray(), configuration)

    return StructureItem(
        fileName = fileName,
        `package` = syntheticItem.`package`,
        moduleName = syntheticItem.moduleName,
        qualifier = syntheticItem.qualifier,
        hasRuntime = syntheticItem.hasRuntime,
        imports = syntheticItem.imports,
    )
}

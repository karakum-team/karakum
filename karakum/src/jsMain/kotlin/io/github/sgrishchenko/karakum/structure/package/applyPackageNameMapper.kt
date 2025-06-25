package io.github.sgrishchenko.karakum.structure.`package`

import io.github.sgrishchenko.karakum.configuration.Configuration
import io.github.sgrishchenko.karakum.util.applyMapper
import js.array.ReadonlyArray
import kotlinx.js.JsPlainObject
import node.path.path

@JsPlainObject
external interface PackageMappingResult {
    val fileName: String
    val `package`: ReadonlyArray<String>
}

fun applyPackageNameMapper(
    packageChunks: ReadonlyArray<String>,
    fileName: String,
    configuration: Configuration,
): PackageMappingResult {
    val packageNameMapper = configuration.packageNameMapper

    val filePath = packageToFileName(packageChunks, fileName)

    val mappedFilePath = applyMapper(filePath, packageNameMapper)

    val dirName = path.dirname(mappedFilePath)
    val baseName = path.basename(mappedFilePath)

    return PackageMappingResult(
        fileName = baseName,
        `package` = dirNameToPackage(dirName)
    )
}

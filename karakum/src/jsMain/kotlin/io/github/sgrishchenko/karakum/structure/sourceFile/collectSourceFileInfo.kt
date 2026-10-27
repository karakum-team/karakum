package io.github.sgrishchenko.karakum.structure.sourceFile

import io.github.sgrishchenko.karakum.configuration.Configuration
import io.github.sgrishchenko.karakum.structure.InputStructureItem
import io.github.sgrishchenko.karakum.structure.InputStructureItemMeta
import io.github.sgrishchenko.karakum.structure.import.ImportInfo
import js.array.ReadonlyArray
import kotlinx.js.JsPlainObject
import typescript.SourceFile

@JsPlainObject
external interface InputSourceFileInfoItem : SourceFileInfoItem, InputStructureItem

typealias SourceFileInfo = ReadonlyArray<InputSourceFileInfoItem>

fun collectSourceFileInfo(
    sourceFiles: ReadonlyArray<SourceFile>,
    importInfo: ImportInfo,
    configuration: Configuration,
): SourceFileInfo {
    return sourceFiles.map { sourceFile ->
        val imports = importInfo[sourceFile] ?: emptyArray()

        val item = createSourceFileInfoItem(
            sourceFile.fileName,
            imports,
            configuration,
        )

        val nodes = sourceFile.statements

        InputSourceFileInfoItem(
            fileName = item.fileName,
            `package` = item.`package`,
            moduleName = item.moduleName,
            qualifier = item.qualifier,
            hasRuntime = item.hasRuntime,
            imports = item.imports,

            nodes = nodes,
            meta = InputStructureItemMeta(
                type = "Source File",
                name = sourceFile.fileName
            ),
        )
    }.toTypedArray()
}

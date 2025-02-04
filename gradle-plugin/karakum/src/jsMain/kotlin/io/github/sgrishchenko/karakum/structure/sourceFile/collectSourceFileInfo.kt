package io.github.sgrishchenko.karakum.structure.sourceFile

import io.github.sgrishchenko.karakum.configuration.Configuration
import io.github.sgrishchenko.karakum.structure.InputStructureItem
import io.github.sgrishchenko.karakum.structure.InputStructureItemMeta
import io.github.sgrishchenko.karakum.structure.import.ImportInfo
import js.array.ReadonlyArray
import js.objects.JsPlainObject
import js.objects.recordOf
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

        // TODO: create ticket for JsPlainObject
        recordOf(
            "fileName" to item.fileName,
            "package" to item.`package`,
            "moduleName" to item.moduleName,
            "qualifier" to item.qualifier,
            "hasRuntime" to item.hasRuntime,
            "imports" to item.imports,

            "nodes" to nodes,
            "meta" to InputStructureItemMeta(
                type = "Source File",
                name = sourceFile.fileName
            ),
        ).unsafeCast<InputSourceFileInfoItem>()
    }.toTypedArray()
}

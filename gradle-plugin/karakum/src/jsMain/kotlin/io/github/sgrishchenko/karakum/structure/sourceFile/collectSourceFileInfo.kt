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
        recordOf<String, Any?>().apply {
            this["fileName"] = item.fileName
            this["package"] = item.`package`
            this["moduleName"] = item.moduleName
            this["qualifier"] = item.qualifier
            this["hasRuntime"] = item.hasRuntime
            this["imports"] = item.imports

            this["nodes"] = nodes
            this["meta"] = InputStructureItemMeta(
                type = "Source File",
                name = sourceFile.fileName
            )
        }.unsafeCast<InputSourceFileInfoItem>()
    }.toTypedArray()
}

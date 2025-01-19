package io.github.sgrishchenko.karakum.structure.namespace

import io.github.sgrishchenko.karakum.configuration.Configuration
import io.github.sgrishchenko.karakum.structure.InputStructureItem
import io.github.sgrishchenko.karakum.structure.InputStructureItemMeta
import io.github.sgrishchenko.karakum.structure.import.ImportInfo
import io.github.sgrishchenko.karakum.util.traverse
import js.array.ReadonlyArray
import js.objects.JsPlainObject
import js.objects.recordOf
import typescript.*

@JsPlainObject
external interface InputNamespaceInfoItem : NamespaceInfoItem, InputStructureItem

typealias NamespaceInfo = ReadonlyArray<InputNamespaceInfoItem>

fun collectNamespaceInfo(
    sourceFiles: ReadonlyArray<SourceFile>,
    importInfo: ImportInfo,
    configuration: Configuration,
): NamespaceInfo {
    val result = mutableListOf<InputNamespaceInfoItem>()

    sourceFiles.forEach { sourceFile ->
        traverse(sourceFile) { node ->
            if (isModuleDeclaration(node)) {
                val imports = importInfo[node] ?: emptyArray()

                val item = createNamespaceInfoItem(
                    node,
                    sourceFile.fileName,
                    imports,
                    configuration,
                )

                var nodes: ReadonlyArray<Node> = emptyArray()

                val body = node.body

                if (body != null && isModuleBlock(body)) {
                    nodes = body.statements.asArray()
                }

                // TODO: create ticket for JsPlainObject
                result += recordOf<String, Any?>().apply {
                    this["fileName"] = item.fileName
                    this["package"] = item.`package`
                    this["moduleName"] = item.moduleName
                    this["qualifier"] = item.qualifier
                    this["hasRuntime"] = item.hasRuntime
                    this["imports"] = item.imports

                    this["name"] = item.name
                    this["strategy"] = item.strategy

                    this["nodes"] = nodes
                    this["meta"] = InputStructureItemMeta(
                        type = "Namespace",
                        name = item.name,
                    )
                }.unsafeCast<InputNamespaceInfoItem>()
            }
        }
    }

    return result.toTypedArray()
}

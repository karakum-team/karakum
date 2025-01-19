package io.github.sgrishchenko.karakum.structure.namespace

import io.github.sgrishchenko.karakum.configuration.Configuration
import io.github.sgrishchenko.karakum.structure.InputStructureItem
import io.github.sgrishchenko.karakum.structure.InputStructureItemMeta
import io.github.sgrishchenko.karakum.structure.import.ImportInfo
import io.github.sgrishchenko.karakum.util.traverse
import js.array.ReadonlyArray
import js.objects.JsPlainObject
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

                result += InputNamespaceInfoItem(
                    fileName = item.fileName,
                    `package` = item.`package`,
                    moduleName = item.moduleName,
                    qualifier = item.qualifier,
                    hasRuntime = item.hasRuntime,
                    imports = item.imports,

                    name = item.name,
                    strategy = item.strategy,

                    nodes = nodes,
                    meta = InputStructureItemMeta(
                        type = "Namespace",
                        name = item.name
                    ),
                )
            }
        }
    }

    return result.toTypedArray()
}

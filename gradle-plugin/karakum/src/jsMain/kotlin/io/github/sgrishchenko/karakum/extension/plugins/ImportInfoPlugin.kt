package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.ConverterContext
import io.github.sgrishchenko.karakum.extension.ConverterPlugin
import io.github.sgrishchenko.karakum.extension.GeneratedFile
import io.github.sgrishchenko.karakum.extension.Render
import io.github.sgrishchenko.karakum.structure.import.ImportInfo
import js.array.ReadonlyArray
import js.symbol.Symbol
import typescript.ModuleDeclaration
import typescript.Node
import typescript.Program
import typescript.isImportDeclaration

@OptIn(ExperimentalJsExport::class)
@JsExport
val importInfoServiceKey = Symbol()

class ImportInfoService(
    private val program: Program,
    private val importInfo: ImportInfo,
) {
    fun resolveImports(sourceFileName: String, node: ModuleDeclaration?): ReadonlyArray<String> {
        if (node != null) {
            return importInfo[node] ?: emptyArray()
        } else {
            val sourcefile = program.getSourceFile(sourceFileName) ?: return emptyArray()

            return importInfo[sourcefile] ?: emptyArray()
        }
    }
}

class ImportInfoPlugin(program: Program, importInfo: ImportInfo) : ConverterPlugin<Node> {
    private val importInfoService = ImportInfoService(program, importInfo)

    override fun setup(context: ConverterContext) {
        context.registerService(importInfoServiceKey, importInfoService)
    }

    override fun traverse(node: Node, context: ConverterContext) = Unit

    override fun render(node: Node, context: ConverterContext, next: Render<Node>): String? {
        if (isImportDeclaration(node)) {
            val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
            checkCoverageService?.deepCover(node)

            return ""
        }

        return null
    }

    override fun generate(context: ConverterContext, render: Render<Node>) = emptyArray<GeneratedFile>()
}

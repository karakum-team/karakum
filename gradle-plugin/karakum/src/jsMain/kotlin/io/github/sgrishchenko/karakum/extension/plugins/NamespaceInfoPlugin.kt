package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.extension.ConverterContext
import io.github.sgrishchenko.karakum.extension.ConverterPlugin
import io.github.sgrishchenko.karakum.extension.GeneratedFile
import io.github.sgrishchenko.karakum.extension.Render
import io.github.sgrishchenko.karakum.structure.namespace.NamespaceInfo
import io.github.sgrishchenko.karakum.structure.namespace.extractNamespaceName
import js.symbol.Symbol
import typescript.ModuleDeclaration
import typescript.Node

@OptIn(ExperimentalJsExport::class)
@JsExport
val namespaceInfoServiceKey = Symbol()

@OptIn(ExperimentalJsExport::class)
@JsExport
class NamespaceInfoService @JsExport.Ignore constructor(namespaceInfo: NamespaceInfo) {
    private val namespaceInfo = namespaceInfo.associate { it.name to it.strategy }

    fun resolveNamespaceStrategy(node: ModuleDeclaration): NamespaceStrategy? {
        val name = extractNamespaceName(node)
        val detailedName = name.joinToString(separator = ".") { it.detailedName }

        return namespaceInfo[detailedName]
    }
}

class NamespaceInfoPlugin(namespaceInfo: NamespaceInfo) : ConverterPlugin<Node> {
    private val namespaceInfoService = NamespaceInfoService(namespaceInfo)

    override fun traverse(node: Node, context: ConverterContext) = Unit

    override fun render(node: Node, context: ConverterContext, next: Render<Node>) = null

    override fun generate(context: ConverterContext, render: Render<Node>) = emptyArray<GeneratedFile>()

    override fun setup(context: ConverterContext) {
        context.registerService(namespaceInfoServiceKey, this.namespaceInfoService)
    }
}

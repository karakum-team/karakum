package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.structure.namespace.NamespaceInfo
import io.github.sgrishchenko.karakum.structure.namespace.extractNamespaceName
import io.github.sgrishchenko.karakum.structure.namespace.resolveDefaultNamespaceStrategy
import typescript.ModuleDeclaration
import typescript.Node

@JsExport
val namespaceInfoServiceKey = ContextKey<NamespaceInfoService>()

@JsExport
class NamespaceInfoService @JsExport.Ignore constructor(namespaceInfo: NamespaceInfo) {
    private val namespaceInfo = namespaceInfo.associate { it.name to it.strategy }

    fun resolveNamespaceStrategy(node: ModuleDeclaration): NamespaceStrategy {
        val name = extractNamespaceName(node)
        val detailedName = name.joinToString(separator = ".") { it.detailedName }

        return namespaceInfo[detailedName] ?: resolveDefaultNamespaceStrategy(node)
    }
}

class NamespaceInfoPlugin(namespaceInfo: NamespaceInfo) : Plugin {
    private val namespaceInfoService = NamespaceInfoService(namespaceInfo)

    override fun traverse(node: Node, context: Context) = Unit

    override fun render(node: Node, context: Context, next: Render<Node>) = null

    override fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()

    override fun setup(context: Context) {
        context.registerService(namespaceInfoServiceKey, this.namespaceInfoService)
    }
}

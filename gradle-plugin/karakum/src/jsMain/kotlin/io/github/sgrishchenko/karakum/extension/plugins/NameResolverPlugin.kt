package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.*
import js.array.ReadonlyArray
import js.symbol.Symbol
import typescript.Node

val nameResolverServiceKey = Symbol()

class NameResolverService(nameResolvers: ReadonlyArray<NameResolver<Node>>) {
    private val nameResolvers = nameResolvers + defaultNameResolvers
    private val resolvedNodes = mutableMapOf<Node, String>()
    private var counter = 0

    fun tryResolveName(node: Node, context: ConverterContext): String? {
        for (nameResolver in nameResolvers) {
            val result = nameResolver(node, context)

            if (result != null) return result
        }

        return null
    }

    fun resolveName(node: Node, context: ConverterContext): String {
        val resolvedName = resolvedNodes[node]
        if (resolvedName != null) return resolvedName

        val result = tryResolveName(node, context) ?: "Temp${counter++}"

        resolvedNodes[node] = result
        return result
    }
}

class NameResolverPlugin(nameResolvers: ReadonlyArray<NameResolver<Node>>) : ConverterPlugin<Node> {
    private val nameResolverService = NameResolverService(nameResolvers)

    override fun setup(context: ConverterContext) {
        context.registerService(nameResolverServiceKey, nameResolverService)
    }

    override fun traverse(node: Node, context: ConverterContext) = Unit

    override fun render(node: Node, context: ConverterContext, next: Render<Node>) = null

    override fun generate(context: ConverterContext, render: Render<Node>) = emptyArray<GeneratedFile>()
}

package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.*
import js.array.ReadonlyArray
import typescript.Node

@JsExport
val nameResolverServiceKey = ContextKey<NameResolverService>()

@JsExport
class NameResolverService @JsExport.Ignore constructor(nameResolvers: ReadonlyArray<NameResolver>) {
    private val nameResolvers = nameResolvers + defaultNameResolvers
    private val resolvedNodes = mutableMapOf<Node, String>()
    private var counter = 0

    fun tryResolveName(node: Node, context: Context): String? {
        for (nameResolver in nameResolvers) {
            val result = nameResolver(node, context)

            if (result != null) return result
        }

        return null
    }

    fun resolveName(node: Node, context: Context): String {
        val resolvedName = resolvedNodes[node]
        if (resolvedName != null) return resolvedName

        val result = tryResolveName(node, context) ?: "Temp${counter++}"

        resolvedNodes[node] = result
        return result
    }
}

class NameResolverPlugin(nameResolvers: ReadonlyArray<NameResolver>) : Plugin {
    private val nameResolverService = NameResolverService(nameResolvers)

    override fun setup(context: Context) {
        context.registerService(nameResolverServiceKey, nameResolverService)
    }

    override fun traverse(node: Node, context: Context) = Unit

    override fun render(node: Node, context: Context, next: Render<Node>) = null

    override fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
}

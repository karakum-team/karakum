package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.*
import js.coroutines.promise
import js.promise.Promise
import typescript.Node
import web.abort.Abortable
import web.abort.asCoroutineScope

val nameResolverServiceKey = ContextKey<NameResolverService>()

@JsExport
@JsName("nameResolverServiceKey")
val jsNameResolverServiceKey = ContextKey<JsNameResolverService>()

class NameResolverService(nameResolvers: List<NameResolver>) {
    private val nameResolvers = nameResolvers + defaultNameResolvers
    private val resolvedNodes = mutableMapOf<Node, String>()
    private var counter = 0

    suspend fun tryResolveName(node: Node, context: Context): String? {
        for (nameResolver in nameResolvers) {
            val result = nameResolver(node, context)

            if (result != null) return result
        }

        return null
    }

    suspend fun resolveName(node: Node, context: Context): String {
        val resolvedName = resolvedNodes[node]
        if (resolvedName != null) return resolvedName

        val result = tryResolveName(node, context) ?: "Temp${counter++}"

        resolvedNodes[node] = result
        return result
    }
}

@JsExport
@JsName("NameResolverService")
class JsNameResolverService @JsExport.Ignore constructor(
    private val delegate: NameResolverService,
) {
    fun tryResolveName(node: Node, context: Context, options: Abortable): Promise<String?> {
        return options.asCoroutineScope().promise {
            delegate.tryResolveName(node, context)
        }
    }

    fun resolveName(node: Node, context: Context, options: Abortable): Promise<String> {
        return options.asCoroutineScope().promise {
            delegate.resolveName(node, context)
        }
    }
}

class NameResolverPlugin(nameResolvers: List<NameResolver>) : Plugin {
    private val nameResolverService = NameResolverService(nameResolvers)
    private val jsNameResolverService = JsNameResolverService(nameResolverService)

    override suspend fun setup(context: Context) {
        context.registerService(nameResolverServiceKey, nameResolverService)
        context.registerService(jsNameResolverServiceKey, jsNameResolverService)
    }

    override suspend fun traverse(node: Node, context: Context) = Unit

    override suspend fun render(node: Node, context: Context, next: Render<Node>) = null

    override suspend fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
}

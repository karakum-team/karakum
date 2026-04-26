package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.*
import js.array.ReadonlyArray
import js.coroutines.promise
import js.promise.Promise
import typescript.Node
import web.abort.Abortable
import web.abort.asCoroutineScope

val injectionServiceKey = ContextKey<InjectionService>()

@JsExport
@JsName("injectionServiceKey")
val jsInjectionServiceKey = ContextKey<JsInjectionService>()

class InjectionService(private val injections: List<Injection>) {
    suspend fun resolveInjections(
        node: Node,
        type: InjectionType,
        context: Context,
        render: Render<Node>,
    ): ReadonlyArray<String> {
        val injectionContext = object : InjectionContext, Context by context {
            override val type = type
        }

        return internalResolveInjections(node, injectionContext, render)
    }

    private suspend fun internalResolveInjections(
        node: Node,
        context: InjectionContext,
        render: Render<Node>
    ): ReadonlyArray<String> {
        val injections = mutableListOf<String>()

        for (injection in this.injections) {
            val result = injection.inject(node, context, render)

            if (result != null) injections += result
        }

        return injections.toTypedArray()
    }
}

@JsExport
@JsName("InjectionService")
class JsInjectionService @JsExport.Ignore constructor(
    private val delegate: InjectionService,
) {
    fun resolveInjections(
        node: Node,
        type: InjectionType,
        context: Context,
        render: Render<Node>,
        options: Abortable,
    ): Promise<ReadonlyArray<String>> {
        return options.asCoroutineScope().promise {
            delegate.resolveInjections(node, type, context, render)
        }
    }
}

class InjectionPlugin(injections: List<Injection>) : Plugin {
    private val injectionService = InjectionService(injections)
    private val jsInjectionService = JsInjectionService(injectionService)

    override suspend fun traverse(node: Node, context: Context) = Unit

    override suspend fun render(node: Node, context: Context, next: Render<Node>) = null

    override suspend fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()

    override suspend fun setup(context: Context) {
        context.registerService(injectionServiceKey, this.injectionService)
        context.registerService(jsInjectionServiceKey, this.jsInjectionService)
    }
}

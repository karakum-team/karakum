package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.*
import js.array.ReadonlyArray
import typescript.Node

@JsExport
val injectionServiceKey = ContextKey<InjectionService>()

@JsExport
class InjectionService @JsExport.Ignore constructor(private val injections: ReadonlyArray<Injection>) {
    fun resolveInjections(
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

    private fun internalResolveInjections(
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

class InjectionPlugin(injections: ReadonlyArray<Injection>) : Plugin {
    private val injectionService = InjectionService(injections)

    override fun traverse(node: Node, context: Context) = Unit

    override fun render(node: Node, context: Context, next: Render<Node>) = null

    override fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()

    override fun setup(context: Context) {
        context.registerService(injectionServiceKey, this.injectionService)
    }
}

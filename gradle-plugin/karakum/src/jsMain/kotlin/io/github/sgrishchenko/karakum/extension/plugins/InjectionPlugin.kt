package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.*
import js.array.ReadonlyArray
import js.symbol.Symbol
import typescript.Node

@OptIn(ExperimentalJsExport::class)
@JsExport
val injectionServiceKey = Symbol()

class InjectionService(private val injections: ReadonlyArray<Injection<Node, Node>>) {
    fun resolveInjections(
        node: Node,
        type: InjectionType,
        context: ConverterContext,
        render: Render<Node>,
    ): ReadonlyArray<String> {
        val injectionContext = object : InjectionContext, ConverterContext by context {
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

class InjectionPlugin(injections: ReadonlyArray<Injection<Node, Node>>) : ConverterPlugin<Node> {
    private val injectionService = InjectionService(injections)

    override fun traverse(node: Node, context: ConverterContext) = Unit

    override fun render(node: Node, context: ConverterContext, next: Render<Node>) = null

    override fun generate(context: ConverterContext, render: Render<Node>) = emptyArray<GeneratedFile>()

    override fun setup(context: ConverterContext) {
        context.registerService(injectionServiceKey, this.injectionService)
    }
}

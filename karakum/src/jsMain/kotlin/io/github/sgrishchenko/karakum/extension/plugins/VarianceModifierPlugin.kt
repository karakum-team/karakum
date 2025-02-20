package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.*
import js.array.ReadonlyArray
import js.symbol.Symbol
import typescript.Node

@OptIn(ExperimentalJsExport::class)
@JsExport
val varianceModifierServiceKey = Symbol()

@OptIn(ExperimentalJsExport::class)
@JsExport
class VarianceModifierService @JsExport.Ignore constructor(
    private val varianceModifiers: ReadonlyArray<VarianceModifier<Node>>
) {
    fun resolveVarianceModifier(
        node: Node,
        context: ConverterContext,
    ): String? {
        for (varianceModifier in varianceModifiers) {
            val result = varianceModifier(node, context)

            if (result != null) return result
        }

        return null
    }
}

class VarianceModifierPlugin(varianceModifiers: ReadonlyArray<VarianceModifier<Node>>) : ConverterPlugin<Node> {
    private val varianceModifierService = VarianceModifierService(varianceModifiers);

    override fun setup(context: ConverterContext) {
        context.registerService(varianceModifierServiceKey, varianceModifierService)
    }

    override fun traverse(node: Node, context: ConverterContext) = Unit

    override fun render(node: Node, context: ConverterContext, next: Render<Node>) = null

    override fun generate(context: ConverterContext, render: Render<Node>) = emptyArray<GeneratedFile>()
}

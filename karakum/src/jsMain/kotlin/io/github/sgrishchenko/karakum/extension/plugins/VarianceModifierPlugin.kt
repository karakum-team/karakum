package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.*
import js.array.ReadonlyArray
import typescript.Node

@JsExport
val varianceModifierServiceKey = ContextKey<VarianceModifierService>()

@JsExport
class VarianceModifierService @JsExport.Ignore constructor(
    private val varianceModifiers: ReadonlyArray<VarianceModifier>
) {
    fun resolveVarianceModifier(
        node: Node,
        context: Context,
    ): String? {
        for (varianceModifier in varianceModifiers) {
            val result = varianceModifier(node, context)

            if (result != null) return result
        }

        return null
    }
}

class VarianceModifierPlugin(varianceModifiers: ReadonlyArray<VarianceModifier>) : Plugin {
    private val varianceModifierService = VarianceModifierService(varianceModifiers)

    override fun setup(context: Context) {
        context.registerService(varianceModifierServiceKey, varianceModifierService)
    }

    override fun traverse(node: Node, context: Context) = Unit

    override fun render(node: Node, context: Context, next: Render<Node>) = null

    override fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
}

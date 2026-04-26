package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.*
import js.array.ReadonlyArray
import typescript.Node

@JsExport
val varianceModifierServiceKey = ContextKey<VarianceModifierService>()

@JsExport
class VarianceModifierService @JsExport.Ignore constructor(
    private val varianceModifiers: List<VarianceModifier>
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

class VarianceModifierPlugin(varianceModifiers: List<VarianceModifier>) : Plugin {
    private val varianceModifierService = VarianceModifierService(varianceModifiers)

    override suspend fun setup(context: Context) {
        context.registerService(varianceModifierServiceKey, varianceModifierService)
    }

    override suspend fun traverse(node: Node, context: Context) = Unit

    override suspend fun render(node: Node, context: Context, next: Render<Node>) = null

    override suspend fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
}

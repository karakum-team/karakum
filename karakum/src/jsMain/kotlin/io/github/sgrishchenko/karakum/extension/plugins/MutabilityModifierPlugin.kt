package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.*
import js.array.ReadonlyArray
import typescript.Node

@JsExport
val mutabilityModifierServiceKey = ContextKey<MutabilityModifierService>()

@JsExport
class MutabilityModifierService @JsExport.Ignore constructor(private val mutabilityModifiers: ReadonlyArray<MutabilityModifier>) {
    fun resolveMutabilityModifier(
        node: Node,
        context: Context,
    ): String? {
        for (mutabilityModifier in mutabilityModifiers) {
            val result = mutabilityModifier(node, context)

            if (result != null) return result
        }

        return null
    }
}

class MutabilityModifierPlugin(mutabilityModifiers: ReadonlyArray<MutabilityModifier>) : Plugin {
    private val mutabilityModifierService = MutabilityModifierService(mutabilityModifiers)

    override fun setup(context: Context) {
        context.registerService(mutabilityModifierServiceKey, this.mutabilityModifierService)
    }

    override fun traverse(node: Node, context: Context) = Unit

    override fun render(node: Node, context: Context, next: Render<Node>) = null

    override fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
}

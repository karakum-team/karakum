package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.*
import js.coroutines.promise
import js.promise.Promise
import typescript.Node
import web.abort.Abortable
import web.abort.asCoroutineScope

val mutabilityModifierServiceKey = ContextKey<MutabilityModifierService>()

@JsExport
@JsName("mutabilityModifierServiceKey")
val jsMutabilityModifierServiceKey = ContextKey<JsMutabilityModifierService>()

class MutabilityModifierService(private val mutabilityModifiers: List<MutabilityModifier>) {
    suspend fun resolveMutabilityModifier(
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

@JsExport
@JsName("MutabilityModifierService")
class JsMutabilityModifierService @JsExport.Ignore constructor(
    private val delegate: MutabilityModifierService,
) {
    fun resolveMutabilityModifier(
        node: Node,
        context: Context,
        options: Abortable,
    ): Promise<String?> {
        return options.asCoroutineScope().promise {
            delegate.resolveMutabilityModifier(node, context)
        }
    }
}

class MutabilityModifierPlugin(mutabilityModifiers: List<MutabilityModifier>) : Plugin {
    private val mutabilityModifierService = MutabilityModifierService(mutabilityModifiers)
    private val jsMutabilityModifierService = JsMutabilityModifierService(mutabilityModifierService)

    override suspend fun setup(context: Context) {
        context.registerService(mutabilityModifierServiceKey, mutabilityModifierService)
        context.registerService(jsMutabilityModifierServiceKey, jsMutabilityModifierService)
    }

    override suspend fun traverse(node: Node, context: Context) = Unit

    override suspend fun render(node: Node, context: Context, next: Render<Node>) = null

    override suspend fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
}

package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.*
import js.coroutines.promise
import js.promise.Promise
import typescript.Node
import web.abort.Abortable
import web.abort.asCoroutineScope

val varianceModifierServiceKey = ContextKey<VarianceModifierService>()

@JsExport
@JsName("varianceModifierServiceKey")
val jsVarianceModifierServiceKey = ContextKey<JsVarianceModifierService>()

class VarianceModifierService(
    private val varianceModifiers: List<VarianceModifier>
) {
    suspend fun resolveVarianceModifier(
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

@JsExport
@JsName("VarianceModifierService")
class JsVarianceModifierService @JsExport.Ignore constructor(
    private val delegate: VarianceModifierService,
) {
    fun resolveVarianceModifier(
        node: Node,
        context: Context,
        options: Abortable,
    ): Promise<String?> {
        return options.asCoroutineScope().promise {
            delegate.resolveVarianceModifier(node, context)
        }
    }
}

class VarianceModifierPlugin(varianceModifiers: List<VarianceModifier>) : Plugin {
    private val varianceModifierService = VarianceModifierService(varianceModifiers)
    private val jsVarianceModifierService = JsVarianceModifierService(varianceModifierService)

    override suspend fun setup(context: Context) {
        context.registerService(varianceModifierServiceKey, varianceModifierService)
        context.registerService(jsVarianceModifierServiceKey, jsVarianceModifierService)
    }

    override suspend fun traverse(node: Node, context: Context) = Unit

    override suspend fun render(node: Node, context: Context, next: Render<Node>) = null

    override suspend fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
}

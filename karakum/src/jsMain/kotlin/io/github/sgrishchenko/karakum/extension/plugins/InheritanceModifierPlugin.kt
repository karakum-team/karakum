package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.*
import js.coroutines.promise
import js.promise.Promise
import typescript.Node
import web.abort.Abortable
import web.abort.asCoroutineScope

val inheritanceModifierServiceKey = ContextKey<InheritanceModifierService>()

@JsExport
@JsName("inheritanceModifierServiceKey")
val jsInheritanceModifierServiceKey = ContextKey<JsInheritanceModifierService>()

class InheritanceModifierService(private val inheritanceModifiers: List<InheritanceModifier>) {
    suspend fun resolveSignatureInheritanceModifier(
        node: Node,
        signature: Signature,
        context: Context,
    ): String? {
        val inheritanceModifierContext = object : InheritanceModifierContext, Context by context {
            override val signature = signature
            override val getter = false
            override val setter = false
        }

        return internalResolveInheritanceModifier(node, inheritanceModifierContext)
    }

    suspend fun resolveGetterInheritanceModifier(
        node: Node,
        context: Context,
    ): String? {
        val inheritanceModifierContext = object : InheritanceModifierContext, Context by context {
            override val signature = null
            override val getter = true
            override val setter = false
        }

        return internalResolveInheritanceModifier(node, inheritanceModifierContext)
    }

    suspend fun resolveSetterInheritanceModifier(
        node: Node,
        context: Context,
    ): String? {
        val inheritanceModifierContext = object : InheritanceModifierContext, Context by context {
            override val signature = null
            override val getter = false
            override val setter = true
        }

        return internalResolveInheritanceModifier(node, inheritanceModifierContext)
    }

    suspend fun resolveInheritanceModifier(
        node: Node,
        context: Context,
    ): String? {
        val inheritanceModifierContext = object : InheritanceModifierContext, Context by context {
            override val signature = null
            override val getter = false
            override val setter = false
        }

        return internalResolveInheritanceModifier(node, inheritanceModifierContext)
    }

    private suspend fun internalResolveInheritanceModifier(
        node: Node,
        context: InheritanceModifierContext
    ): String? {
        for (inheritanceModifier in inheritanceModifiers) {
            val result = inheritanceModifier(node, context)

            if (result != null) return result
        }

        return null
    }
}

@JsExport
@JsName("InheritanceModifierService")
class JsInheritanceModifierService @JsExport.Ignore constructor(
    private val delegate: InheritanceModifierService,
) {
    fun resolveSignatureInheritanceModifier(
        node: Node,
        signature: Signature,
        context: Context,
        options: Abortable,
    ): Promise<String?> {
        return options.asCoroutineScope().promise {
            delegate.resolveSignatureInheritanceModifier(node, signature, context)
        }
    }

    fun resolveGetterInheritanceModifier(
        node: Node,
        context: Context,
        options: Abortable,
    ): Promise<String?> {
        return options.asCoroutineScope().promise {
            delegate.resolveGetterInheritanceModifier(node, context)
        }
    }

    fun resolveSetterInheritanceModifier(
        node: Node,
        context: Context,
        options: Abortable,
    ): Promise<String?> {
        return options.asCoroutineScope().promise {
            delegate.resolveSetterInheritanceModifier(node, context)
        }
    }

    fun resolveInheritanceModifier(
        node: Node,
        context: Context,
        options: Abortable,
    ): Promise<String?> {
        return options.asCoroutineScope().promise {
            delegate.resolveInheritanceModifier(node, context)
        }
    }
}

class InheritanceModifierPlugin(inheritanceModifiers: List<InheritanceModifier>) : Plugin {
    private val inheritanceModifierService = InheritanceModifierService(inheritanceModifiers)
    private val jsInheritanceModifierService = JsInheritanceModifierService(inheritanceModifierService)

    override suspend fun setup(context: Context) {
        context.registerService(inheritanceModifierServiceKey, inheritanceModifierService)
        context.registerService(jsInheritanceModifierServiceKey, jsInheritanceModifierService)
    }

    override suspend fun traverse(node: Node, context: Context) = Unit

    override suspend fun render(node: Node, context: Context, next: Render<Node>) = null

    override suspend fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
}

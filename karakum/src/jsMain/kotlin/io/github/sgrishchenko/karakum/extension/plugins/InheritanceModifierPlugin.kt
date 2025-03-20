package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.*
import js.array.ReadonlyArray
import js.symbol.Symbol
import typescript.Node

@OptIn(ExperimentalJsExport::class)
@JsExport
val inheritanceModifierServiceKey = Symbol()

@OptIn(ExperimentalJsExport::class)
@JsExport
class InheritanceModifierService @JsExport.Ignore constructor(private val inheritanceModifiers: ReadonlyArray<InheritanceModifier<Node>>) {
    fun resolveSignatureInheritanceModifier(
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

    fun resolveGetterInheritanceModifier(
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

    fun resolveSetterInheritanceModifier(
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

    fun resolveInheritanceModifier(
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

    private fun internalResolveInheritanceModifier(
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

class InheritanceModifierPlugin(inheritanceModifiers: ReadonlyArray<InheritanceModifier<Node>>) : ConverterPlugin<Node> {
    private val inheritanceModifierService = InheritanceModifierService(inheritanceModifiers)

    override fun setup(context: Context) {
        context.registerService(inheritanceModifierServiceKey, this.inheritanceModifierService)
    }

    override fun traverse(node: Node, context: Context) = Unit

    override fun render(node: Node, context: Context, next: Render<Node>) = null

    override fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
}

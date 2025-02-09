package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.*
import js.array.ReadonlyArray
import js.symbol.Symbol
import typescript.Node

@OptIn(ExperimentalJsExport::class)
@JsExport
val inheritanceModifierServiceKey = Symbol()

class InheritanceModifierService(private val inheritanceModifiers: ReadonlyArray<InheritanceModifier<Node>>) {
    fun resolveSignatureInheritanceModifier(
        node: Node,
        signature: Signature,
        context: ConverterContext,
    ): String? {
        val inheritanceModifierContext = object : InheritanceModifierContext, ConverterContext by context {
            override val signature = signature
            override val getter = false
            override val setter = false
        }

        return internalResolveInheritanceModifier(node, inheritanceModifierContext)
    }

    fun resolveGetterInheritanceModifier(
        node: Node,
        context: ConverterContext,
    ): String? {
        val inheritanceModifierContext = object : InheritanceModifierContext, ConverterContext by context {
            override val signature = null
            override val getter = true
            override val setter = false
        }

        return internalResolveInheritanceModifier(node, inheritanceModifierContext)
    }

    fun resolveSetterInheritanceModifier(
        node: Node,
        context: ConverterContext,
    ): String? {
        val inheritanceModifierContext = object : InheritanceModifierContext, ConverterContext by context {
            override val signature = null
            override val getter = false
            override val setter = true
        }

        return internalResolveInheritanceModifier(node, inheritanceModifierContext)
    }

    fun resolveInheritanceModifier(
        node: Node,
        context: ConverterContext,
    ): String? {
        val inheritanceModifierContext = object : InheritanceModifierContext, ConverterContext by context {
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

    override fun setup(context: ConverterContext) {
        context.registerService(inheritanceModifierServiceKey, this.inheritanceModifierService)
    }

    override fun traverse(node: Node, context: ConverterContext) = Unit

    override fun render(node: Node, context: ConverterContext, next: Render<Node>) = null

    override fun generate(context: ConverterContext, render: Render<Node>) = emptyArray<GeneratedFile>()
}

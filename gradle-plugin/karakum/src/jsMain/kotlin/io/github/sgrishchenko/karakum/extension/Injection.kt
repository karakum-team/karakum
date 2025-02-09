package io.github.sgrishchenko.karakum.extension

import js.array.ReadonlyArray
import seskar.js.JsNative
import typescript.Node

@OptIn(ExperimentalJsExport::class)
@JsExport
enum class InjectionType {
    MEMBER,
    STATIC_MEMBER,

    PARAMETER,
    TYPE_PARAMETER,

    HERITAGE_CLAUSE,
}

@OptIn(ExperimentalJsExport::class)
@JsExport
external interface InjectionContext : ConverterContext {
    val type: InjectionType
}

external interface Injection<in TNode : Node, in TInjectionNode : Node> : ConverterPlugin<TNode> {
    fun inject(node: TInjectionNode, context: InjectionContext, render: Render<Node>): ReadonlyArray<String>?
}

external interface SimpleInjection<in TInjectionNode : Node> {
    @JsNative
    operator fun invoke(node: TInjectionNode, context: InjectionContext, next: Render<Node>): ReadonlyArray<String>?
}

fun <TInjectionNode : Node> createSimpleInjection(
    inject: (node: TInjectionNode, context: InjectionContext, next: Render<Node>) -> ReadonlyArray<String>?,
) = createSimpleInjection<Node, _>(inject.unsafeCast<SimpleInjection<TInjectionNode>>())

fun <TNode : Node, TInjectionNode : Node> createSimpleInjection(
    inject: SimpleInjection<TInjectionNode>,
): Injection<TNode, TInjectionNode> {
    return object : Injection<TNode, TInjectionNode> {
        override fun setup(context: ConverterContext) = Unit

        override fun traverse(node: Node, context: ConverterContext) = Unit

        override fun render(node: TNode, context: ConverterContext, next: Render<Node>) = null

        override fun inject(node: TInjectionNode, context: InjectionContext, render: Render<Node>) = inject(node, context, render)

        override fun generate(context: ConverterContext, render: Render<Node>) = emptyArray<GeneratedFile>()
    }
}

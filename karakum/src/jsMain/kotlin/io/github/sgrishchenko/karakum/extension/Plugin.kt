package io.github.sgrishchenko.karakum.extension

import js.array.ReadonlyArray
import seskar.js.JsNative
import typescript.Node

@OptIn(ExperimentalJsExport::class)
@JsExport
external interface ConverterPlugin<in TNode : Node> {
    fun setup(context: Context)

    fun traverse(node: Node, context: Context)

    fun render(node: TNode, context: Context, next: Render<Node>): String?

    fun generate(context: Context, render: Render<Node>): ReadonlyArray<GeneratedFile>
}

external interface SimpleConverterPlugin<in TNode : Node> {
    @JsNative
    operator fun invoke(node: TNode, context: Context, next: Render<Node>): String?
}

fun <TNode : Node> createSimplePlugin(
    render: (node: TNode, context: Context, next: Render<Node>) -> String?
) = createSimplePlugin(render.unsafeCast<SimpleConverterPlugin<TNode>>())

fun <TNode : Node> createSimplePlugin(
    render: SimpleConverterPlugin<TNode>
): ConverterPlugin<TNode> {
    return object : ConverterPlugin<TNode> {
        override fun setup(context: Context) = Unit

        override fun traverse(node: Node, context: Context) = Unit

        override fun render(node: TNode, context: Context, next: Render<Node>) = render(node, context, next)

        override fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
    }
}

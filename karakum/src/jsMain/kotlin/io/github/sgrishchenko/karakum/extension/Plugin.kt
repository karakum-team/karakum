package io.github.sgrishchenko.karakum.extension

import js.array.ReadonlyArray
import seskar.js.JsNative
import typescript.Node

@OptIn(ExperimentalJsExport::class)
@JsExport
external interface Plugin<in TNode : Node> {
    fun setup(context: Context)

    fun traverse(node: Node, context: Context)

    fun render(node: TNode, context: Context, next: Render<Node>): String?

    fun generate(context: Context, render: Render<Node>): ReadonlyArray<GeneratedFile>
}

external interface SimplePlugin<in TNode : Node> {
    @JsNative
    operator fun invoke(node: TNode, context: Context, next: Render<Node>): String?
}

fun <TNode : Node> createSimplePlugin(
    render: (node: TNode, context: Context, next: Render<Node>) -> String?
) = createSimplePlugin(render.unsafeCast<SimplePlugin<TNode>>())

fun <TNode : Node> createSimplePlugin(
    render: SimplePlugin<TNode>
): Plugin<TNode> {
    return object : Plugin<TNode> {
        override fun setup(context: Context) = Unit

        override fun traverse(node: Node, context: Context) = Unit

        override fun render(node: TNode, context: Context, next: Render<Node>) = render(node, context, next)

        override fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
    }
}

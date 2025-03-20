package io.github.sgrishchenko.karakum.extension

import js.array.ReadonlyArray
import typescript.Node

@OptIn(ExperimentalJsExport::class)
@JsExport
external interface Plugin {
    fun setup(context: Context)

    fun traverse(node: Node, context: Context)

    fun render(node: Node, context: Context, next: Render<Node>): String?

    fun generate(context: Context, render: Render<Node>): ReadonlyArray<GeneratedFile>
}

typealias SimplePlugin = (node: Node, context: Context, next: Render<Node>) -> String?

fun createSimplePlugin(
    render: SimplePlugin
): Plugin {
    return object : Plugin {
        override fun setup(context: Context) = Unit

        override fun traverse(node: Node, context: Context) = Unit

        override fun render(node: Node, context: Context, next: Render<Node>) = render(node, context, next)

        override fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
    }
}

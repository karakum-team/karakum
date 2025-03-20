package io.github.sgrishchenko.karakum.extension

import js.array.ReadonlyArray
import seskar.js.JsNative
import typescript.Node

@OptIn(ExperimentalJsExport::class)
@JsExport
external interface Plugin {
    fun setup(context: Context)

    fun traverse(node: Node, context: Context)

    fun render(node: Node, context: Context, next: Render<Node>): String?

    fun generate(context: Context, render: Render<Node>): ReadonlyArray<GeneratedFile>
}

external interface SimplePlugin {
    @JsNative
    operator fun invoke(node: Node, context: Context, next: Render<Node>): String?
}

fun createSimplePlugin(
    render: (node: Node, context: Context, next: Render<Node>) -> String?
) = createSimplePlugin(render.unsafeCast<SimplePlugin>())

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

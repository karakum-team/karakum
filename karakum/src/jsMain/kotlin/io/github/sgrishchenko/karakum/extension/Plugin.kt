package io.github.sgrishchenko.karakum.extension

import io.github.sgrishchenko.karakum.util.currentAbortable
import js.array.ReadonlyArray
import js.core.Void
import js.coroutines.promise
import js.promise.Promise
import js.promise.await
import typescript.Node
import web.abort.Abortable
import web.abort.asCoroutineScope

interface Plugin {
    suspend fun setup(context: Context)

    suspend fun traverse(node: Node, context: Context)

    suspend fun render(node: Node, context: Context, next: Render<Node>): String?

    suspend fun generate(context: Context, render: Render<Node>): ReadonlyArray<GeneratedFile>
}

@JsExport
@JsName("Plugin")
external interface JsPlugin {
    fun setup(context: Context, options: Abortable): Promise<Unit>

    fun traverse(node: Node, context: Context, options: Abortable): Promise<Unit>

    fun render(node: Node, context: Context, next: Render<Node>, options: Abortable): Promise<String?>

    fun generate(context: Context, render: Render<Node>, options: Abortable): Promise<ReadonlyArray<GeneratedFile>>
}

typealias SimplePlugin = suspend (node: Node, context: Context, next: Render<Node>) -> String?

typealias SimpleJsPlugin = (node: Node, context: Context, next: Render<Node>, options: Abortable) -> Promise<String?>

fun createPlugin(
    render: SimplePlugin
): Plugin {
    return object : Plugin {
        override suspend fun setup(context: Context) = Unit

        override suspend fun traverse(node: Node, context: Context) = Unit

        override suspend fun render(node: Node, context: Context, next: Render<Node>) = render(node, context, next)

        override suspend fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
    }
}

fun createJsPlugin(
    render: SimpleJsPlugin
): JsPlugin {
    return object : JsPlugin {
        override fun setup(context: Context, options: Abortable) = Promise.resolve(Unit)

        override fun traverse(node: Node, context: Context, options: Abortable) = Promise.resolve(Unit)

        override fun render(node: Node, context: Context, next: Render<Node>, options: Abortable) =
            render(node, context, next, options)

        override fun generate(context: Context, render: Render<Node>, options: Abortable) =
            Promise.resolve(emptyArray<GeneratedFile>())
    }
}

fun JsPlugin.toPlugin(): Plugin {
    val jsPlugin = this

    return object : Plugin {
        override suspend fun setup(context: Context) =
            jsPlugin.setup(context, currentAbortable()).await()

        override suspend fun traverse(
            node: Node,
            context: Context,
        ) =
            jsPlugin.traverse(node, context, currentAbortable()).await()

        override suspend fun render(
            node: Node,
            context: Context,
            next: Render<Node>,
        ): String? =
            jsPlugin.render(node, context, next, currentAbortable()).await()

        override suspend fun generate(
            context: Context,
            render: Render<Node>,
        ): ReadonlyArray<GeneratedFile> =
            jsPlugin.generate(context, render, currentAbortable()).await()
    }
}

fun Plugin.toJsPlugin(): JsPlugin {
    val plugin = this

    return object : JsPlugin {
        override fun setup(
            context: Context,
            options: Abortable,
        ): Promise<Unit> =
            options.asCoroutineScope().promise {
                plugin.setup(context)
            }

        override fun traverse(
            node: Node,
            context: Context,
            options: Abortable,
        ): Promise<Unit> =
            options.asCoroutineScope().promise {
                plugin.traverse(node, context)
            }

        override fun render(
            node: Node,
            context: Context,
            next: Render<Node>,
            options: Abortable,
        ): Promise<String?> =
            options.asCoroutineScope().promise {
                plugin.render(node, context, next)
            }

        override fun generate(
            context: Context,
            render: Render<Node>,
            options: Abortable,
        ): Promise<ReadonlyArray<GeneratedFile>> =
            options.asCoroutineScope().promise {
                plugin.generate(context, render)
            }
    }
}

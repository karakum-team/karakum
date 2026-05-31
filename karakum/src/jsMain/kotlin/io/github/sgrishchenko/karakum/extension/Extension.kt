package io.github.sgrishchenko.karakum.extension

import io.github.sgrishchenko.karakum.util.currentAbortable
import kotlinx.coroutines.await
import kotlinx.coroutines.promise
import typescript.Node
import web.abort.Abortable
import web.abort.asCoroutineScope
import kotlin.js.Promise

internal typealias Extension<C> = suspend (node: Node, context: C) -> String?

internal typealias JsExtension<C> = (node: Node, context: C, options: Abortable) -> Promise<String?>

internal fun <C : Context> JsExtension<C>.toExtension(): Extension<C> =
    { node, context -> this(node, context, currentAbortable()).await() }

internal fun <C : Context> Extension<C>.toJsExtension(): JsExtension<C> =
    { node, context, options ->
        val extension = this

        options.asCoroutineScope().promise {
            extension(node, context)
        }
    }

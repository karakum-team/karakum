package io.github.sgrishchenko.karakum.extension

import typescript.Node
import web.abort.Abortable
import kotlin.js.Promise

typealias VarianceModifier = suspend (node: Node, context: Context) -> String?

typealias JsVarianceModifier = (node: Node, context: Context, options: Abortable) -> Promise<String?>

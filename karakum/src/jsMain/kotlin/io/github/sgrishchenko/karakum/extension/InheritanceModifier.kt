package io.github.sgrishchenko.karakum.extension

import io.github.sgrishchenko.karakum.extension.plugins.Signature
import typescript.Node
import web.abort.Abortable
import kotlin.js.Promise

@JsExport
external interface InheritanceModifierContext : Context {
    val signature: Signature?
    val getter: Boolean?
    val setter: Boolean?
}

typealias InheritanceModifier = suspend (node: Node, context: InheritanceModifierContext) -> String?

typealias JsInheritanceModifier = (node: Node, context: InheritanceModifierContext, options: Abortable) -> Promise<String?>

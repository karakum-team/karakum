package io.github.sgrishchenko.karakum.extension

import io.github.sgrishchenko.karakum.extension.plugins.Signature
import typescript.Node

@JsExport
external interface InheritanceModifierContext : Context {
    val signature: Signature?
    val getter: Boolean?
    val setter: Boolean?
}

typealias InheritanceModifier = (node: Node, context: InheritanceModifierContext) -> String?

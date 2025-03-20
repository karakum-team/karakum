package io.github.sgrishchenko.karakum.extension

import io.github.sgrishchenko.karakum.extension.plugins.Signature
import seskar.js.JsNative
import typescript.Node

@OptIn(ExperimentalJsExport::class)
@JsExport
external interface InheritanceModifierContext : Context {
    val signature: Signature?
    val getter: Boolean?
    val setter: Boolean?
}

@OptIn(ExperimentalJsExport::class)
@JsExport
external interface InheritanceModifier {
    @JsNative
    operator fun invoke(node: Node, context: InheritanceModifierContext): String?
}

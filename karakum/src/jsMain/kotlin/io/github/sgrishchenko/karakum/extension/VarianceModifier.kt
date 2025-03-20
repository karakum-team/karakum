package io.github.sgrishchenko.karakum.extension

import seskar.js.JsNative
import typescript.Node

@OptIn(ExperimentalJsExport::class)
@JsExport
external interface VarianceModifier {
    @JsNative
    operator fun invoke(node: Node, context: Context): String?
}

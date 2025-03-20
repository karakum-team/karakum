package io.github.sgrishchenko.karakum.extension

import seskar.js.JsNative
import typescript.Node

@OptIn(ExperimentalJsExport::class)
@JsExport
external interface VarianceModifier<in TNode : Node> {
    @JsNative
    operator fun invoke(node: TNode, context: Context): String?
}

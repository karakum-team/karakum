package io.github.sgrishchenko.karakum.extension

import seskar.js.JsNative
import typescript.Node

external interface NameResolver<in TNode : Node> {
    @JsNative
    operator fun invoke(node: TNode, context: ConverterContext): String
}

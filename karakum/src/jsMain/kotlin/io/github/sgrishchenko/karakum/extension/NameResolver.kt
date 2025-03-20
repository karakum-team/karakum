package io.github.sgrishchenko.karakum.extension

import seskar.js.JsNative
import typescript.Node

@OptIn(ExperimentalJsExport::class)
@JsExport
external interface NameResolver<in TNode : Node> {
    @JsNative
    operator fun invoke(node: TNode, context: Context): String?
}

fun <TNode : Node> NameResolver(resolver: (node: TNode, context: Context) -> String?) =
    resolver.unsafeCast<NameResolver<TNode>>()

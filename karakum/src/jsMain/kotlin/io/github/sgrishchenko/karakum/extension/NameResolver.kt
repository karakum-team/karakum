package io.github.sgrishchenko.karakum.extension

import seskar.js.JsNative
import typescript.Node

@OptIn(ExperimentalJsExport::class)
@JsExport
external interface NameResolver {
    @JsNative
    operator fun invoke(node: Node, context: Context): String?
}

fun NameResolver(resolver: (node: Node, context: Context) -> String?) =
    resolver.unsafeCast<NameResolver>()

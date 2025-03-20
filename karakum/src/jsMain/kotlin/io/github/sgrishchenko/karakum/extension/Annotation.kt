package io.github.sgrishchenko.karakum.extension

import seskar.js.JsNative
import typescript.Node

@OptIn(ExperimentalJsExport::class)
@JsExport
external interface AnnotationContext : Context {
    val isAnonymousDeclaration: Boolean
}

@OptIn(ExperimentalJsExport::class)
@JsExport
external interface Annotation<in TNode : Node> {
    @JsNative
    operator fun invoke(node: TNode, context: AnnotationContext): String?
}

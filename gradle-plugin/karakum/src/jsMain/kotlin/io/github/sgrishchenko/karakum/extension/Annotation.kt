package io.github.sgrishchenko.karakum.extension

import seskar.js.JsNative
import typescript.Node

external interface AnnotationContext : ConverterContext {
    val isAnonymousDeclaration: Boolean
}

external interface Annotation<in TNode : Node> {
    @JsNative
    operator fun invoke(node: TNode, context: AnnotationContext): String
}

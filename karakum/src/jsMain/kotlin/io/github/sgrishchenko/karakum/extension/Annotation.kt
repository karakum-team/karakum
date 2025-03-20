package io.github.sgrishchenko.karakum.extension

import typescript.Node

@OptIn(ExperimentalJsExport::class)
@JsExport
external interface AnnotationContext : Context {
    val isAnonymousDeclaration: Boolean
}

typealias Annotation = (node: Node, context: AnnotationContext) -> String?

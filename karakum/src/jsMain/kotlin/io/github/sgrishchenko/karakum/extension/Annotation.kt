package io.github.sgrishchenko.karakum.extension

import typescript.Node

@JsExport
external interface AnnotationContext : Context {
    val isAnonymousDeclaration: Boolean
}

typealias Annotation = (node: Node, context: AnnotationContext) -> String?

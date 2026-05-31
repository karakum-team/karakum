package io.github.sgrishchenko.karakum.extension

import typescript.Node
import web.abort.Abortable
import kotlin.js.Promise

@JsExport
external interface AnnotationContext : Context {
    val isAnonymousDeclaration: Boolean
}

typealias Annotation = suspend (node: Node, context: AnnotationContext) -> String?

typealias JsAnnotation = (node: Node, context: AnnotationContext, options: Abortable) -> Promise<String?>

package io.github.sgrishchenko.karakum.extension

external interface AnnotationContext : ConverterContext {
    val isAnonymousDeclaration: Boolean
}

typealias Annotation<TNode/* : ts.Node */> =
            (node: TNode, context: AnnotationContext) -> String

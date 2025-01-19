package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.*
import js.array.ReadonlyArray
import js.symbol.Symbol
import typescript.Node

val annotationServiceKey = Symbol()

class AnnotationService(private val annotations: ReadonlyArray<Annotation<Node>>) {
    fun resolveAnonymousAnnotations(node: Node, context: ConverterContext): ReadonlyArray<String> {
        return internalResolveAnnotations(node, true, context)
    }

    fun resolveAnnotations(node: Node, context: ConverterContext): ReadonlyArray<String> {
        return internalResolveAnnotations(node, false, context)
    }

    private fun internalResolveAnnotations(
        node: Node,
        isAnonymousDeclaration: Boolean,
        context: ConverterContext,
    ): ReadonlyArray<String> {
        val annotationContext = object : AnnotationContext, ConverterContext by context {
            override val isAnonymousDeclaration = isAnonymousDeclaration
        }

        val annotations = mutableListOf<String>()

        for (annotation in this.annotations) {
            val result = annotation(node, annotationContext)

            if (result != null) annotations += result
        }

        return annotations.toTypedArray()
    }
}

class AnnotationPlugin(annotations: ReadonlyArray<Annotation<Node>>) : ConverterPlugin<Node> {
    private val annotationService = AnnotationService(annotations)

    override fun setup(context: ConverterContext) {
        context.registerService(annotationServiceKey, annotationService)
    }

    override fun traverse(node: Node, context: ConverterContext) = Unit

    override fun generate(context: ConverterContext, render: Render<Node>) = emptyArray<GeneratedFile>()

    override fun render(node: Node, context: ConverterContext, next: Render<Node>): String? {
        val annotations = annotationService.resolveAnnotations(node, context)

        if (annotations.isNotEmpty()) {
            return """
${annotations.joinToString(separator = "\n")}
${next(node)}
            """.trim();
        }

        return null
    }


}

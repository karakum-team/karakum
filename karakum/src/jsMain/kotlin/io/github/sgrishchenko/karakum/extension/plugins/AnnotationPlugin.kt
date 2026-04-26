package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.*
import js.array.ReadonlyArray
import typescript.Node

@JsExport
val annotationServiceKey = ContextKey<AnnotationService>()

@JsExport
class AnnotationService @JsExport.Ignore constructor(private val annotations: List<Annotation>) {
    fun resolveAnonymousAnnotations(node: Node, context: Context): ReadonlyArray<String> {
        return internalResolveAnnotations(node, true, context)
    }

    fun resolveAnnotations(node: Node, context: Context): ReadonlyArray<String> {
        return internalResolveAnnotations(node, false, context)
    }

    private fun internalResolveAnnotations(
        node: Node,
        isAnonymousDeclaration: Boolean,
        context: Context,
    ): ReadonlyArray<String> {
        val annotationContext = object : AnnotationContext, Context by context {
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

class AnnotationPlugin(annotations: List<Annotation>) : Plugin {
    private val annotationService = AnnotationService(annotations)

    override suspend fun setup(context: Context) {
        context.registerService(annotationServiceKey, annotationService)
    }

    override suspend fun traverse(node: Node, context: Context) = Unit

    override suspend fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()

    override suspend fun render(node: Node, context: Context, next: Render<Node>): String? {
        val annotations = annotationService.resolveAnnotations(node, context)

        if (annotations.isNotEmpty()) {
            return """
${annotations.joinToString(separator = "\n")}
${next(node)}
            """.trim()
        }

        return null
    }


}

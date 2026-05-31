package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.*
import js.array.ReadonlyArray
import js.coroutines.promise
import typescript.Node
import web.abort.Abortable
import web.abort.asCoroutineScope
import js.promise.Promise

val annotationServiceKey = ContextKey<AnnotationService>()

@JsExport
@JsName("annotationServiceKey")
val jsAnnotationServiceKey = ContextKey<JsAnnotationService>()

class AnnotationService(private val annotations: List<Annotation>) {
    suspend fun resolveAnonymousAnnotations(node: Node, context: Context): ReadonlyArray<String> {
        return internalResolveAnnotations(node, true, context)
    }

    suspend fun resolveAnnotations(node: Node, context: Context): ReadonlyArray<String> {
        return internalResolveAnnotations(node, false, context)
    }

    private suspend fun internalResolveAnnotations(
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

@JsExport
@JsName("AnnotationService")
class JsAnnotationService @JsExport.Ignore constructor(
    private val delegate: AnnotationService,
) {
    fun resolveAnonymousAnnotations(node: Node, context: Context, options: Abortable): Promise<ReadonlyArray<String>> {
        return options.asCoroutineScope().promise {
            delegate.resolveAnonymousAnnotations(node, context)
        }
    }

    fun resolveAnnotations(node: Node, context: Context, options: Abortable): Promise<ReadonlyArray<String>> {
        return options.asCoroutineScope().promise {
            delegate.resolveAnnotations(node, context)
        }
    }
}

class AnnotationPlugin(annotations: List<Annotation>) : Plugin {
    private val annotationService = AnnotationService(annotations)
    private val jsAnnotationService = JsAnnotationService(annotationService)

    override suspend fun setup(context: Context) {
        context.registerService(annotationServiceKey, annotationService)
        context.registerService(jsAnnotationServiceKey, jsAnnotationService)
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

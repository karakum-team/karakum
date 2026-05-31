package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.structure.derived.DerivedDeclaration
import io.github.sgrishchenko.karakum.structure.derived.generateDerivedDeclarations
import io.github.sgrishchenko.karakum.util.currentAbortable
import io.github.sgrishchenko.karakum.util.getSourceFileOrNull
import js.array.ReadonlyArray
import js.promise.Promise
import js.promise.await
import kotlinx.js.JsPlainObject
import typescript.Node
import web.abort.Abortable

external interface AnonymousDeclaration

@JsPlainObject
internal external interface AnonymousDeclarationRenderResult {
    val name: String
    val declaration: String
    val reference: String
}

@Suppress("NOTHING_TO_INLINE")
inline fun AnonymousDeclaration(result: String) =
    result.unsafeCast<AnonymousDeclaration>()

@Suppress("NOTHING_TO_INLINE")
inline fun AnonymousDeclaration(
    name: String,
    declaration: String,
    reference: String,
) =
    AnonymousDeclarationRenderResult(
        name = name,
        declaration = declaration,
        reference = reference,
    ).unsafeCast<AnonymousDeclaration>()

typealias AnonymousDeclarationRender = suspend (
    node: Node,
    context: Context,
    render: Render<Node>,
) -> AnonymousDeclaration?

class AnonymousDeclarationPlugin(
    render: AnonymousDeclarationRender,
) : Plugin {
    private val anonymousDeclarationRender = render
    private val generated = mutableMapOf<Node, DerivedDeclaration>()

    override suspend fun setup(context: Context) = Unit

    override suspend fun traverse(node: Node, context: Context) = Unit

    override suspend fun render(node: Node, context: Context, next: Render<Node>): String? {
        val typeScriptService = context.requireService(typeScriptServiceKey)
        val annotationService = context.requireService(annotationServiceKey)

        val annotations = annotationService.resolveAnonymousAnnotations(node, context)

        val result = anonymousDeclarationRender(node, context, next)

        if (result == null) return result
        if (jsTypeOf(result) == "string") return result.toString()

        val renderResult = result.unsafeCast<AnonymousDeclarationRenderResult>()
        val name = renderResult.name
        val declaration = renderResult.declaration
        val reference = renderResult.reference

        val sourceFileName = node.getSourceFileOrNull()?.fileName ?: "generated.d.ts"
        val namespace = typeScriptService.findClosestNamespace(node)

        generated[node] = DerivedDeclaration(
            sourceFileName = sourceFileName,
            namespace = namespace,
            fileName = "${name}.kt",
            body = (annotations + declaration).joinToString(separator = "\n"),
        )

        return reference
    }

    override suspend fun generate(context: Context, render: Render<Node>): ReadonlyArray<DerivedFile> {
        return generateDerivedDeclarations(generated.values.toTypedArray(), context)
    }
}

fun createAnonymousDeclarationPlugin(
    render: AnonymousDeclarationRender,
): Plugin {
    return AnonymousDeclarationPlugin(render)
}

@JsExport
@JsName("createAnonymousDeclarationPlugin")
fun createAnonymousDeclarationPluginAsync(
    render: (
        node: Node,
        context: Context,
        render: Render<Node>,
        options: Abortable,
    ) -> Promise<AnonymousDeclaration?>,
): JsPlugin =
    AnonymousDeclarationPlugin { node, context, next ->
        render(node, context, next, currentAbortable()).await()
    }.toJsPlugin()

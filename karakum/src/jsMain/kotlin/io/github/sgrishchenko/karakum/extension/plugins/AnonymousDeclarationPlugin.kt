package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.Context
import io.github.sgrishchenko.karakum.extension.DerivedFile
import io.github.sgrishchenko.karakum.extension.Plugin
import io.github.sgrishchenko.karakum.extension.Render
import io.github.sgrishchenko.karakum.structure.derived.DerivedDeclaration
import io.github.sgrishchenko.karakum.structure.derived.generateDerivedDeclarations
import io.github.sgrishchenko.karakum.util.getSourceFileOrNull
import js.array.ReadonlyArray
import js.objects.JsPlainObject
import typescript.Node

@OptIn(ExperimentalJsExport::class)
@JsExport
external interface AnonymousDeclarationContext : Context {
    fun resolveName(node: Node): String
}

external interface AnonymousDeclaration

@PublishedApi
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

typealias AnonymousDeclarationRender = (
    node: Node,
    context: AnonymousDeclarationContext,
    render: Render<Node>,
) -> AnonymousDeclaration?

class AnonymousDeclarationPlugin(
    private val anonymousDeclarationRender: AnonymousDeclarationRender
) : Plugin {
    private val generated = mutableMapOf<Node, DerivedDeclaration>()

    override fun setup(context: Context) = Unit

    override fun traverse(node: Node, context: Context) = Unit

    override fun render(node: Node, context: Context, next: Render<Node>): String? {
        val nameResolverService = context.lookupService<NameResolverService>(nameResolverServiceKey)
        if (nameResolverService == null) error("AnonymousDeclarationPlugin can't work without NameResolverService")

        val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
        if (typeScriptService == null) error("AnonymousDeclarationPlugin can't work without TypeScriptService")

        val annotationService = context.lookupService<AnnotationService>(annotationServiceKey)
        if (annotationService == null) error("AnonymousDeclarationPlugin can't work without AnnotationService")

        val annotations = annotationService.resolveAnonymousAnnotations(node, context)

        val anonymousDeclarationContext = object : AnonymousDeclarationContext, Context by context {
            override fun resolveName(node: Node) = nameResolverService.resolveName(node, context)
        }

        val result = anonymousDeclarationRender(
            node,
            anonymousDeclarationContext,
            next
        )

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

    override fun generate(context: Context, render: Render<Node>): ReadonlyArray<DerivedFile> {
        return generateDerivedDeclarations(generated.values.toTypedArray(), context)
    }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun createAnonymousDeclarationPlugin(
    render: AnonymousDeclarationRender,
): Plugin {
    return AnonymousDeclarationPlugin(render)
}

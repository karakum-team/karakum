package io.github.sgrishchenko.karakum.extension.plugins.configurable

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.extension.plugins.*
import io.github.sgrishchenko.karakum.util.currentAbortable
import io.github.sgrishchenko.karakum.util.escapeIdentifier
import js.promise.Promise
import js.promise.await
import kotlinx.js.JsPlainObject
import typescript.*
import web.abort.Abortable
import io.github.sgrishchenko.karakum.extension.plugins.configurable.isPromiseType as defaultIsPromiseType
import io.github.sgrishchenko.karakum.extension.plugins.configurable.renderPromisePayload as defaultRenderPayload

class PromiseMethodPlugin(
    private val isPromiseType: List<Matcher<Context>> = match(::defaultIsPromiseType),
    private val ignore: List<Matcher<Context>> = emptyList(),
    private val exclude: List<Matcher<SignatureContext>> = emptyList(),
    private val renderPayload: suspend (TypeReferenceNode, Context, Render<Node>) -> String = ::defaultRenderPayload,
) : Plugin {
    constructor(
        renderPayload: suspend (TypeReferenceNode, Context, Render<Node>) -> String = ::defaultRenderPayload,
    ) : this(
        isPromiseType = match(::defaultIsPromiseType),
        ignore = emptyList(),
        exclude = emptyList(),
        renderPayload = renderPayload
    )

    constructor(
        isPromiseType: suspend (Node, Context) -> Boolean = ::defaultIsPromiseType,
        ignore: suspend (Node, Context) -> Boolean = { _, _ -> false },
        exclude: suspend (Node, SignatureContext) -> Boolean = { _, _ -> false },
        renderPayload: suspend (TypeReferenceNode, Context, Render<Node>) -> String = ::defaultRenderPayload,
    ) : this(
        isPromiseType = match(isPromiseType),
        ignore = match(ignore),
        exclude = match(exclude),
        renderPayload = renderPayload,
    )

    override suspend fun setup(context: Context) = Unit

    override suspend fun traverse(node: Node, context: Context) = Unit

    override suspend fun render(node: Node, context: Context, next: Render<Node>): String? {
        if (!isMethodSignature(node) && !isMethodDeclaration(node)) return null

        if (isMethodSignature(node) && node.questionToken != null) return null
        if (isMethodDeclaration(node) && node.questionToken != null) return null

        @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
        node as SignatureDeclarationBase

        val type = node.type ?: return null
        if (!isPromiseType.matches(type, context)) return null
        require(isTypeReferenceNode(type))

        if (ignore.matches(node, context)) return null

        val inheritanceModifierService = context.lookupService(inheritanceModifierServiceKey)

        val nameNode = node.name ?: return null

        val name = escapeIdentifier(next(nameNode))
        val annotation = createKebabAnnotation(nameNode)
            .takeIf { it.isNotEmpty() }
            ?: "@JsName(\"$name\")"

        val typeParameters = node.typeParameters?.asArray()
            ?.map { next(it) }
            ?.filter { it.isNotEmpty() }
            ?.joinToString(separator = ", ")

        val returnType = node.type?.let{ next(it) }

        val returnTypePayload = renderPayload(type, context, next)

        val promiseDeclaration = convertParameterDeclarations(
            node, context, next,
            ParameterDeclarationStrategy.function,
        ) template@{ parameters, signature ->
            val signatureContext = object : SignatureContext, Context by context {
                override val signature = signature
            }

            if (exclude.matches(node, signatureContext)) return@template ""

            val inheritanceModifier =
                inheritanceModifierService?.resolveSignatureInheritanceModifier(node, signature, context)

            """
                $annotation
                ${ifPresent(inheritanceModifier) { "$it "}}fun ${ifPresent(typeParameters) { "<${it}> " }}${name}Async(${parameters})${ifPresent(returnType) { ": $it" }}
            """.trimIndent()
        }

        val suspendDeclaration = convertParameterDeclarations(
            node, context, next,
            ParameterDeclarationStrategy.function,
        ) template@{ parameters, signature ->
            val signatureContext = object : SignatureContext, Context by context {
                override val signature = signature
            }

            if (exclude.matches(node, signatureContext)) return@template ""

            val inheritanceModifier =
                inheritanceModifierService?.resolveSignatureInheritanceModifier(node, signature, context)

            """
                @seskar.js.JsAsync
                ${ifPresent(inheritanceModifier) { "$it " }}suspend fun ${ifPresent(typeParameters) { "<${it}> " }}${name}(${parameters})${ifPresent(returnTypePayload) { ": $it"}}
            """.trimIndent()
        }

        return "${promiseDeclaration}\n\n${suspendDeclaration}"
    }

    override suspend fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
}

@JsExport
@JsPlainObject
external interface PromiseMethodPluginConfiguration {
    val isPromiseType: ((Node, Context, Abortable) -> Promise<Boolean>)?
    val ignore: ((Node, Context, Abortable) -> Promise<Boolean>)?
    val exclude: ((Node, SignatureContext, Abortable) -> Promise<Boolean>)?
    val renderPayload: ((TypeReferenceNode, Context, Render<Node>, Abortable) -> Promise<String>)?
}

@JsExport
fun createPromiseMethodPlugin(configuration: PromiseMethodPluginConfiguration): JsPlugin =
    PromiseMethodPlugin(
        isPromiseType = configuration.isPromiseType?.wrap() ?: { node, context ->
            defaultIsPromiseType(node, context)
        },
        ignore = configuration.ignore?.wrap() ?: { _, _ -> false },
        exclude = configuration.exclude?.wrap() ?: { _, _ -> false },
        renderPayload = { node, context, render ->
            configuration.renderPayload
                ?.invoke(node, context, render, currentAbortable())
                ?.await()
                ?: defaultRenderPayload(node, context, render)
        },
    ).toJsPlugin()

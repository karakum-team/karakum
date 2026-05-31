package io.github.sgrishchenko.karakum.extension.plugins.configurable

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.extension.plugins.*
import io.github.sgrishchenko.karakum.structure.derived.DerivedDeclaration
import io.github.sgrishchenko.karakum.structure.derived.generateDerivedDeclarations
import io.github.sgrishchenko.karakum.util.currentAbortable
import io.github.sgrishchenko.karakum.util.getSourceFileOrNull
import js.array.ReadonlyArray
import js.promise.Promise
import js.promise.await
import kotlinx.js.JsPlainObject
import typescript.*
import web.abort.Abortable
import io.github.sgrishchenko.karakum.extension.plugins.configurable.isPromiseType as defaultIsPromiseType
import io.github.sgrishchenko.karakum.extension.plugins.configurable.renderPromisePayload as defaultRenderPayload

class PromiseFunctionPlugin(
    private val isPromiseType: List<Matcher<Context>> = match(::defaultIsPromiseType),
    private val ignore: List<Matcher<Context>> = emptyList(),
    private val exclude: List<Matcher<SignatureContext>> = emptyList(),
    private val renderPayload: suspend (TypeReferenceNode, Context, Render<Node>) -> String = ::defaultRenderPayload,
) : Plugin {
    private val promiseApiDeclarations = mutableListOf<DerivedDeclaration>()

    constructor(
        renderPayload: suspend (TypeReferenceNode, Context, Render<Node>) -> String = ::defaultRenderPayload,
    ) : this(
        isPromiseType = match(::defaultIsPromiseType),
        ignore = emptyList(),
        exclude = emptyList(),
        renderPayload = renderPayload
    )

    constructor(
        isPromiseType: (Node, Context) -> Boolean = ::defaultIsPromiseType,
        ignore: (Node, Context) -> Boolean = { _, _ -> false },
        exclude: (Node, SignatureContext) -> Boolean = { _, _ -> false },
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
        if (!isFunctionDeclaration(node)) return null

        val type = node.type ?: return null
        if (!isPromiseType.matches(type, context)) return null
        require(isTypeReferenceNode(type))

        if (ignore.matches(node, context)) return null

        val sourceFileName = node.getSourceFileOrNull()?.fileName ?: "generated.d.ts"

        val typeScriptService = context.requireService(typeScriptServiceKey)

        val namespace = typeScriptService.findClosestNamespace(node)

        val namespaceInfoService = context.requireService(namespaceInfoServiceKey)

        val externalModifier = namespaceInfoService.resolveExternalModifier(namespace)

        val nameNode = node.name ?: return null

        val name = next(nameNode)

        val typeParameters = node.typeParameters?.asArray()
            ?.map { next(it) }
            ?.filter { it.isNotEmpty() }
            ?.joinToString(separator = ", ")

        val returnType = node.type?.let { next(it) }

        val returnTypePayload = renderPayload(type, context, next)

        val body = convertParameterDeclarations(
            node, context, next,
            ParameterDeclarationStrategy.function,
        ) template@{ parameters, signature ->
            val signatureContext = object : SignatureContext, Context by context {
                override val signature = signature
            }

            if (exclude.matches(node, signatureContext)) return@template ""

            """
                @seskar.js.JsAsync
                ${ifPresent(externalModifier) { "$it " }}suspend fun ${ifPresent(typeParameters) { "<${it}> " }}${name}(${parameters})${ifPresent(returnTypePayload) { ": $it" }
            }
            """.trimIndent()
        }

        val nodeInfo = DerivedDeclaration(
            sourceFileName,
            namespace,
            fileName = "${name}.suspend.kt",
            body,
        )

        promiseApiDeclarations += nodeInfo

        return convertParameterDeclarations(
            node, context, next,
            ParameterDeclarationStrategy.function,
        ) template@{ parameters, signature ->
            val signatureContext = object : SignatureContext, Context by context {
                override val signature = signature
            }

            if (exclude.matches(node, signatureContext)) return@template ""

            """
                @JsName("$name")
                ${ifPresent(externalModifier) { "$it " }}fun ${ifPresent(typeParameters) { "<${it}> " }}${name}Async(${parameters})${ifPresent(returnType) { ": $it" }
            }
            """.trimIndent()
        }
    }

    override suspend fun generate(context: Context, render: Render<Node>): ReadonlyArray<GeneratedFile> {
        return generateDerivedDeclarations(promiseApiDeclarations.toTypedArray(), context)
    }
}

@JsExport
@JsPlainObject
external interface PromiseFunctionPluginConfiguration {
    val isPromiseType: ((Node, Context) -> Boolean)?
    val ignore: ((Node, Context) -> Boolean)?
    val exclude: ((Node, SignatureContext) -> Boolean)?
    val renderPayload: ((TypeReferenceNode, Context, Render<Node>, Abortable) -> Promise<String>)?
}


@JsExport
fun createPromiseFunctionPlugin(configuration: PromiseFunctionPluginConfiguration): JsPlugin =
    PromiseFunctionPlugin(
        isPromiseType = configuration.isPromiseType ?: ::defaultIsPromiseType,
        ignore = configuration.ignore ?: { _, _ -> false },
        exclude = configuration.exclude ?: { _, _ -> false },
        renderPayload = { node, context, render ->
            configuration.renderPayload
                ?.invoke(node, context, render, currentAbortable())
                ?.await()
                ?: defaultRenderPayload(node, context, render)
        },
    ).toJsPlugin()

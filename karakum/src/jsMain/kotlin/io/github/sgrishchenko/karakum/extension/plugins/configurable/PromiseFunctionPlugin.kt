package io.github.sgrishchenko.karakum.extension.plugins.configurable

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.extension.plugins.*
import io.github.sgrishchenko.karakum.extension.plugins.Signature
import io.github.sgrishchenko.karakum.structure.derived.DerivedDeclaration
import io.github.sgrishchenko.karakum.structure.derived.generateDerivedDeclarations
import io.github.sgrishchenko.karakum.util.escapeIdentifier
import io.github.sgrishchenko.karakum.util.getSourceFileOrNull
import js.array.ReadonlyArray
import kotlinx.js.JsPlainObject
import typescript.*
import io.github.sgrishchenko.karakum.extension.plugins.configurable.isPromiseType as defaultIsPromiseType

@JsExport
@JsPlainObject
external interface PromiseFunctionPluginConfiguration {
    val isPromiseType: ((Node, Context) -> Boolean)?
    val ignore: ((Node) -> Boolean)?
    val exclude: ((node: FunctionDeclaration, signature: Signature) -> Boolean)?
    val renderPayload: ((TypeReferenceNode, Context, Render<Node>) -> String)?
}

@JsExport
class PromiseFunctionPlugin(configuration: PromiseFunctionPluginConfiguration) : Plugin {
    private val isPromiseType = configuration.isPromiseType ?: ::defaultIsPromiseType
    private lateinit var ignoreMatchers: List<Matcher>
    private val exclude = configuration.exclude ?: { _, _ -> false }
    private val renderPayload = configuration.renderPayload ?: { node, _, render ->
        val typeArguments = requireNotNull(node.typeArguments)
        render(typeArguments.asArray().first())
    }

    private val promiseApiDeclarations = mutableListOf<DerivedDeclaration>()

    @JsExport.Ignore
    constructor(
        isPromiseType: ((Node, Context) -> Boolean)? = null,
        ignore: ((Node) -> Boolean)? = null,
        exclude: ((node: FunctionDeclaration, signature: Signature) -> Boolean)? = null,
        renderPayload: ((TypeReferenceNode, Context, Render<Node>) -> String)? = null,
    ) : this(
        PromiseFunctionPluginConfiguration(
            isPromiseType,
            ignore,
            exclude,
            renderPayload,
        )
    )

    @JsExport.Ignore
    constructor(
        isPromiseType: ((Node, Context) -> Boolean)? = null,
        ignore: List<Matcher>,
        exclude: ((node: FunctionDeclaration, signature: Signature) -> Boolean)? = null,
        renderPayload: ((TypeReferenceNode, Context, Render<Node>) -> String)? = null,
    ) : this(
        PromiseFunctionPluginConfiguration(
            isPromiseType,
            exclude = exclude,
            renderPayload = renderPayload,
        )
    ) {
        ignoreMatchers = ignore
    }

    init {
        if (!::ignoreMatchers.isInitialized) {
            ignoreMatchers = match {
                configuration.ignore?.let { match(it) }
            }
        }
    }

    override fun setup(context: Context) = Unit

    override fun traverse(node: Node, context: Context) = Unit

    override fun render(node: Node, context: Context, next: Render<Node>): String? {
        if (!isFunctionDeclaration(node)) return null

        val type = node.type ?: return null
        if (!isPromiseType(type, context)) return null
        require(isTypeReferenceNode(type))

        if (ignoreMatchers.any { it.matches(node, context) }) return null

        val sourceFileName = node.getSourceFileOrNull()?.fileName ?: "generated.d.ts"

        val typeScriptService = context.requireService(typeScriptServiceKey)

        val namespace = typeScriptService.findClosestNamespace(node)

        val namespaceInfoService = context.requireService(namespaceInfoServiceKey)

        val externalModifier = namespaceInfoService.resolveExternalModifier(namespace)

        val nameNode = node.name ?: return null

        val name = escapeIdentifier(next(nameNode))

        val typeParameters = node.typeParameters?.asArray()
            ?.map { next(it) }
            ?.filter { it.isNotEmpty() }
            ?.joinToString(separator = ", ")

        val returnType = node.type?.let { next(it) }

        val returnTypePayload = renderPayload(type, context, next)

        val body = convertParameterDeclarations(
            node, context, next,
            ParameterDeclarationsConfiguration(
                strategy = ParameterDeclarationStrategy.function,
                template = template@{ parameters, signature ->
                    if (exclude(node, signature)) return@template ""

                    """
                        @seskar.js.JsAsync
                        ${ifPresent(externalModifier) { "$it " }}suspend fun ${ifPresent(typeParameters) { "<${it}> " }}${name}(${parameters})${ifPresent(returnTypePayload) { ": $it" }
                    }
                    """.trimIndent()
                }
            )
        )

        val nodeInfo = DerivedDeclaration(
            sourceFileName,
            namespace,
            fileName = "${name}.suspend.kt",
            body,
        )

        promiseApiDeclarations += nodeInfo

        return convertParameterDeclarations(
            node, context, next,
            ParameterDeclarationsConfiguration(
                strategy = ParameterDeclarationStrategy.function,
                template = template@{ parameters, signature ->
                    if (exclude(node, signature)) return@template ""

                    """
                        @JsName("$name")
                        ${ifPresent(externalModifier) { "$it " }}fun ${ifPresent(typeParameters) { "<${it}> " }}${name}Async(${parameters})${ifPresent(returnType) { ": $it" }
                    }
                    """.trimIndent()
                }
            )
        )
    }

    override fun generate(context: Context, render: Render<Node>): ReadonlyArray<GeneratedFile> {
        return generateDerivedDeclarations(promiseApiDeclarations.toTypedArray(), context)
    }
}

package io.github.sgrishchenko.karakum.extension.plugins.configurable

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.extension.plugins.*
import io.github.sgrishchenko.karakum.extension.plugins.Signature
import io.github.sgrishchenko.karakum.util.escapeIdentifier
import kotlinx.js.JsPlainObject
import typescript.*
import io.github.sgrishchenko.karakum.extension.plugins.configurable.isPromiseType as defaultIsPromiseType

@JsExport
@JsPlainObject
external interface PromiseMethodPluginConfiguration {
    val isPromiseType: ((Node, Context) -> Boolean)?
    val ignore: ((Node) -> Boolean)?
    val exclude: ((node: SignatureDeclarationBase, signature: Signature) -> Boolean)?
    val renderPayload: ((TypeReferenceNode, Context, Render<Node>) -> String)?
}

@JsExport
class PromiseMethodPlugin(configuration: PromiseMethodPluginConfiguration) : Plugin {
    private val isPromiseType = configuration.isPromiseType ?: ::defaultIsPromiseType
    private lateinit var ignoreMatchers: List<Matcher>
    private val exclude = configuration.exclude ?: { _, _ -> false }
    private val renderPayload = configuration.renderPayload ?: { node, _, render ->
        val typeArguments = requireNotNull(node.typeArguments)
        render(typeArguments.asArray().first())
    }

    @JsExport.Ignore
    constructor(
        isPromiseType: ((Node, Context) -> Boolean)? = null,
        ignore: ((Node) -> Boolean)? = null,
        exclude: ((node: SignatureDeclarationBase, signature: Signature) -> Boolean)? = null,
        renderPayload: ((TypeReferenceNode, Context, Render<Node>) -> String)? = null,
    ) : this(
        PromiseMethodPluginConfiguration(
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
        exclude: ((node: SignatureDeclarationBase, signature: Signature) -> Boolean)? = null,
        renderPayload: ((TypeReferenceNode, Context, Render<Node>) -> String)? = null,
    ) : this(
        PromiseMethodPluginConfiguration(
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
        if (!isMethodSignature(node) && !isMethodDeclaration(node)) return null

        if (isMethodSignature(node) && node.questionToken != null) return null
        if (isMethodDeclaration(node) && node.questionToken != null) return null

        @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
        node as SignatureDeclarationBase

        val type = node.type ?: return null
        if (!isPromiseType(type, context)) return null
        require(isTypeReferenceNode(type))

        if (ignoreMatchers.any { it.matches(node, context) }) return null

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
            ParameterDeclarationsConfiguration(
                strategy = ParameterDeclarationStrategy.function,
                template = template@{ parameters, signature ->
                    if (exclude(node, signature)) return@template ""

                    val inheritanceModifier = inheritanceModifierService?.resolveSignatureInheritanceModifier(node, signature, context)

                    """
                        $annotation
                        ${ifPresent(inheritanceModifier) { "$it "}}fun ${ifPresent(typeParameters) { "<${it}> " }}${name}Async(${parameters})${ifPresent(returnType) { ": $it" }}
                    """.trimIndent()
                }
            )
        )

        val suspendDeclaration = convertParameterDeclarations(
            node, context, next,
            ParameterDeclarationsConfiguration(
                strategy = ParameterDeclarationStrategy.function,
                template = template@{ parameters, signature ->
                    if (exclude(node, signature)) return@template ""

                    val inheritanceModifier = inheritanceModifierService?.resolveSignatureInheritanceModifier(node, signature, context)

                    """
                        @seskar.js.JsAsync
                        ${ifPresent(inheritanceModifier) { "$it " }}suspend fun ${ifPresent(typeParameters) { "<${it}> " }}${name}(${parameters})${ifPresent(returnTypePayload) { ": $it"}}
                    """.trimIndent()
                }
            )
        )

        return "${promiseDeclaration}\n\n${suspendDeclaration}"
    }

    override fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
}

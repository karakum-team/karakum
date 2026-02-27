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
    val ignore: ((Node, Context) -> Boolean)?
    val exclude: ((Node, SignatureContext) -> Boolean)?
    val renderPayload: ((TypeReferenceNode, Context, Render<Node>) -> String)?
}

@JsExport
class PromiseMethodPlugin(configuration: PromiseMethodPluginConfiguration) : Plugin {
    private lateinit var isPromiseTypeMatchers: List<Matcher<Context>>
    private lateinit var ignoreMatchers: List<Matcher<Context>>
    private lateinit var excludeMatchers: List<Matcher<SignatureContext>>
    private val renderPayload = configuration.renderPayload ?: { node, _, render ->
        val typeArguments = requireNotNull(node.typeArguments)
        render(typeArguments.asArray().first())
    }

    @JsExport.Ignore
    constructor(
        renderPayload: ((TypeReferenceNode, Context, Render<Node>) -> String)? = null,
    ) : this(
        PromiseMethodPluginConfiguration(
            renderPayload = renderPayload
        )
    )

    @JsExport.Ignore
    constructor(
        isPromiseType: ((Node, Context) -> Boolean)? = null,
        ignore: ((Node, Context) -> Boolean)? = null,
        exclude: ((Node, SignatureContext) -> Boolean)? = null,
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
        isPromiseType: List<Matcher<Context>>? = null,
        ignore: List<Matcher<Context>>? = null,
        exclude: List<Matcher<SignatureContext>>? = null,
        renderPayload: ((TypeReferenceNode, Context, Render<Node>) -> String)? = null,
    ) : this(
        PromiseMethodPluginConfiguration(
            renderPayload = renderPayload,
        )
    ) {
        isPromiseType?.let { isPromiseTypeMatchers = it }
        ignore?.let { ignoreMatchers = it }
        exclude?.let { excludeMatchers = it }
    }

    init {
        if (!::isPromiseTypeMatchers.isInitialized) {
            isPromiseTypeMatchers = match {
                configuration.isPromiseType?.let { match(it) }
                    ?: match(::defaultIsPromiseType)
            }
        }

        if (!::ignoreMatchers.isInitialized) {
            ignoreMatchers = match {
                configuration.ignore?.let { match(it) }
            }
        }

        if (!::excludeMatchers.isInitialized) {
            excludeMatchers = match {
                configuration.exclude?.let { match(it) }
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
        if (!isPromiseTypeMatchers.matches(type, context)) return null
        require(isTypeReferenceNode(type))

        if (ignoreMatchers.matches(node, context)) return null

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
                    val signatureContext = object : SignatureContext, Context by context {
                        override val signature = signature
                    }

                    if (excludeMatchers.matches(node, signatureContext)) return@template ""

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
                    val signatureContext = object : SignatureContext, Context by context {
                        override val signature = signature
                    }

                    if (excludeMatchers.matches(node, signatureContext)) return@template ""

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

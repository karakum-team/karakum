package io.github.sgrishchenko.karakum.extension.plugins.configurable

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import kotlinx.js.JsPlainObject
import typescript.Node
import typescript.TypeReferenceNode
import typescript.asArray
import typescript.isTypeReferenceNode
import typescript.isUnionTypeNode
import io.github.sgrishchenko.karakum.extension.plugins.configurable.isPromiseType as defaultIsPromiseType

@JsExport
@JsPlainObject
external interface PromiseResultPluginConfiguration {
    val isPromiseType: ((Node, Context) -> Boolean)?
    val ignore: ((Node) -> Boolean)?
    val renderPayload: ((TypeReferenceNode, Context, Render<Node>) -> String)?
}

@JsExport
class PromiseResultPlugin(configuration: PromiseResultPluginConfiguration) : Plugin {
    private val isPromiseType = configuration.isPromiseType ?: ::defaultIsPromiseType
    private lateinit var ignoreMatchers: List<Matcher>
    private val renderPayload = configuration.renderPayload ?: { node, _, render ->
        val typeArguments = requireNotNull(node.typeArguments)
        render(typeArguments.asArray().first())
    }

    @JsExport.Ignore
    constructor(
        isPromiseType: ((Node, Context) -> Boolean)? = null,
        ignore: ((Node) -> Boolean)? = null,
        renderPayload: ((TypeReferenceNode, Context, Render<Node>) -> String)? = null,
    ): this(
        PromiseResultPluginConfiguration(
            isPromiseType,
            ignore,
            renderPayload
        )
    )

    @JsExport.Ignore
    constructor(
        isPromiseType: ((Node, Context) -> Boolean)? = null,
        ignore: List<Matcher>,
        renderPayload: ((TypeReferenceNode, Context, Render<Node>) -> String)? = null,
    ): this(
        PromiseResultPluginConfiguration(
            isPromiseType,
            renderPayload = renderPayload
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
        if (!isUnionTypeNode(node)) return null

        if (node.types.asArray().size != 2) return null
        if (node.types.asArray().none { isPromiseType(it, context) }) return null

        val typeScriptService = context.lookupService(typeScriptServiceKey)

        val parent = typeScriptService?.getParent(node)

        if (parent != null && ignoreMatchers.any { it.matches(parent, context) }) return null

        val promiseType = node.types.asArray().first { isPromiseType(it, context) }
        val otherType = node.types.asArray().first { !isPromiseType(it, context) }

        require(isTypeReferenceNode(promiseType))

        val promisePayload = renderPayload(promiseType, context, next)
        val otherPayload = next(otherType)

        if (promisePayload != otherPayload) return null

        return "js.promise.PromiseResult<${promisePayload}>"
    }

    override fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
}

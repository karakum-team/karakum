package io.github.sgrishchenko.karakum.extension.plugins.configurable

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import kotlinx.js.JsPlainObject
import typescript.Node
import typescript.asArray
import typescript.isTypeReferenceNode
import typescript.isUnionTypeNode
import io.github.sgrishchenko.karakum.extension.plugins.configurable.isPromiseType as defaultIsPromiseType

@JsExport
@JsPlainObject
external interface PromiseResultPluginConfiguration {
    val isPromiseType: ((Node, Context) -> Boolean)?
    val ignore: ((Node) -> Boolean)?
}

@JsExport
class PromiseResultPlugin(configuration: PromiseResultPluginConfiguration) : Plugin {
    private val isPromiseType = configuration.isPromiseType ?: ::defaultIsPromiseType
    private lateinit var ignoreMatchers: List<Matcher>

    @JsExport.Ignore
    constructor(
        isPromiseType: ((Node, Context) -> Boolean)? = null,
        ignore: ((Node) -> Boolean)? = null
    ): this(
        PromiseResultPluginConfiguration(isPromiseType, ignore)
    )

    @JsExport.Ignore
    constructor(
        isPromiseType: ((Node, Context) -> Boolean)? = null,
        ignore: List<Matcher>
    ): this(PromiseResultPluginConfiguration(isPromiseType)) {
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

        if (!isTypeReferenceNode(promiseType)) return null

        val typeArguments = promiseType.typeArguments ?: return null

        val promisePayload = next(typeArguments.asArray().first())
        val otherPayload = next(otherType)

        if (promisePayload != otherPayload) return null

        return "js.promise.PromiseResult<${promisePayload}>"
    }

    override fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
}

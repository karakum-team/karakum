package io.github.sgrishchenko.karakum.extension.plugins.configurable

import io.github.sgrishchenko.karakum.extension.Context
import io.github.sgrishchenko.karakum.extension.GeneratedFile
import io.github.sgrishchenko.karakum.extension.Matcher
import io.github.sgrishchenko.karakum.extension.Plugin
import io.github.sgrishchenko.karakum.extension.Render
import io.github.sgrishchenko.karakum.extension.isBuiltin
import io.github.sgrishchenko.karakum.extension.match
import io.github.sgrishchenko.karakum.extension.matches
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import kotlinx.js.JsPlainObject
import typescript.Node
import typescript.asArray
import typescript.isIdentifier
import typescript.isTypeReferenceNode
import typescript.isUnionTypeNode

private fun isPromiseType(node: Node, context: Context): Boolean {
    if (!isTypeReferenceNode(node)) return false

    val typeName = node.typeName

    return isIdentifier(typeName)
            && typeName.text == "Promise"
            && isBuiltin(typeName, context)
}

@JsExport
@JsPlainObject
external interface PromiseResultPluginConfiguration {
    val ignore: ((Node) -> Boolean)?
}

@JsExport
class PromiseResultPlugin(configuration: PromiseResultPluginConfiguration) : Plugin {
    private lateinit var ignoreMatchers: List<Matcher>

    @JsExport.Ignore
    constructor(ignore: ((Node) -> Boolean)? = null): this(PromiseResultPluginConfiguration(ignore))

    @JsExport.Ignore
    constructor(ignore: List<Matcher>): this(PromiseResultPluginConfiguration()) {
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

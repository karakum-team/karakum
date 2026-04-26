package io.github.sgrishchenko.karakum.extension.plugins.configurable

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.currentAbortable
import js.promise.Promise
import js.promise.await
import kotlinx.js.JsPlainObject
import typescript.*
import web.abort.Abortable
import io.github.sgrishchenko.karakum.extension.plugins.configurable.isPromiseType as defaultIsPromiseType
import io.github.sgrishchenko.karakum.extension.plugins.configurable.renderPromisePayload as defaultRenderPayload

class PromiseResultPlugin(
    private val isPromiseType: List<Matcher<Context>> = match(::defaultIsPromiseType),
    private val ignore: List<Matcher<Context>> = emptyList(),
    private val renderPayload: suspend (TypeReferenceNode, Context, Render<Node>) -> String = ::defaultRenderPayload,
) : Plugin {

    constructor(
        renderPayload: suspend (TypeReferenceNode, Context, Render<Node>) -> String = ::defaultRenderPayload,
    ) : this(
        isPromiseType = match(::defaultIsPromiseType),
        ignore = emptyList(),
        renderPayload = renderPayload
    )

    constructor(
        isPromiseType: (Node, Context) -> Boolean = ::defaultIsPromiseType,
        ignore: (Node, Context) -> Boolean = { _, _ -> false },
        renderPayload: suspend (TypeReferenceNode, Context, Render<Node>) -> String = ::defaultRenderPayload,
    ) : this(
        isPromiseType = match(isPromiseType),
        ignore = match(ignore),
        renderPayload = renderPayload,
    )

    override suspend fun setup(context: Context) = Unit

    override suspend fun traverse(node: Node, context: Context) = Unit

    override suspend fun render(node: Node, context: Context, next: Render<Node>): String? {
        if (!isUnionTypeNode(node)) return null

        if (node.types.asArray().size != 2) return null
        if (node.types.asArray().none { isPromiseType.matches(it, context) }) return null

        val typeScriptService = context.lookupService(typeScriptServiceKey)

        val parent = typeScriptService?.getParent(node)

        if (parent != null && ignore.matches(parent, context)) return null

        val promiseType = node.types.asArray().first { isPromiseType.matches(it, context) }
        val otherType = node.types.asArray().first { !isPromiseType.matches(it, context) }

        require(isTypeReferenceNode(promiseType))

        val promisePayload = renderPayload(promiseType, context, next)
        val otherPayload = next(otherType)

        if (promisePayload != otherPayload) return null

        return "js.promise.PromiseResult<${promisePayload}>"
    }

    override suspend fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()
}

@JsExport
@JsPlainObject
external interface PromiseResultPluginConfiguration {
    val isPromiseType: ((Node, Context) -> Boolean)?
    val ignore: ((Node, Context) -> Boolean)?
    val renderPayload: ((TypeReferenceNode, Context, Render<Node>, Abortable) -> Promise<String>)?
}

@JsExport
fun createPromiseResultPlugin(configuration: PromiseResultPluginConfiguration): JsPlugin =
    PromiseResultPlugin(
        isPromiseType = configuration.isPromiseType ?: ::defaultIsPromiseType,
        ignore = configuration.ignore ?: { _, _ -> false },
        renderPayload = { node, context, render ->
            configuration.renderPayload
                ?.invoke(node, context, render, currentAbortable())
                ?.await()
                ?: defaultRenderPayload(node, context, render)
        },
    ).toJsPlugin()

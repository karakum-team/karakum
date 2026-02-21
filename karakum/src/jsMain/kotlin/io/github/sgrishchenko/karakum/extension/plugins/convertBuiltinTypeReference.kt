package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.Plugin
import io.github.sgrishchenko.karakum.extension.createPlugin
import io.github.sgrishchenko.karakum.extension.isBuiltin
import io.github.sgrishchenko.karakum.util.getParentOrNull
import js.array.component1
import js.array.component2
import js.objects.Object
import js.objects.ReadonlyRecord
import typescript.isExpressionWithTypeArguments
import typescript.isIdentifier
import typescript.isTypeReferenceNode

private val readonlyBuiltinTypeTypes = mapOf(
    "Array" to "js.array.ReadonlyArray",
    "Record" to "js.objects.ReadonlyRecord",
    "Map" to "js.collections.ReadonlyMap",
    "Set" to "js.collections.ReadonlySet",
)

fun createBuiltinTypePlugin(browserApi: ReadonlyRecord<String, String>): Plugin {
    val builtinTypes = Object.entries(browserApi)
        .associate { (key, value) ->  key to value }
        .plus(readonlyBuiltinTypeTypes)

    return createPlugin plugin@{ node, context, _ ->
        if (!isIdentifier(node)) return@plugin null

        val parent = node.getParentOrNull() ?: return@plugin null
        if (
            !isTypeReferenceNode(parent)
            && !isExpressionWithTypeArguments(parent)
        ) return@plugin null

        if (node.text !in builtinTypes) return@plugin null
        if (!isBuiltin(node, context)) return@plugin null

        val checkCoverageService = context.lookupService(checkCoverageServiceKey)
        checkCoverageService?.cover(node)

        builtinTypes.getValue(node.text)
    }
}

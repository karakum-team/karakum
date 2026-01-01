package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import io.github.sgrishchenko.karakum.extension.isBuiltin
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.isExpressionWithTypeArguments
import typescript.isIdentifier
import typescript.isTypeReferenceNode

private val builtinTypes = mapOf(
    "Array" to "js.array.ReadonlyArray",
    "Record" to "js.objects.ReadonlyRecord",
    "Error" to "js.errors.JsError",
    "Iterable" to "js.iterable.JsIterable",
    "Map" to "js.collections.ReadonlyMap",
    "Set" to "js.collections.ReadonlySet",
)

val convertBuiltinTypeReference = createPlugin plugin@{ node, context, _ ->
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

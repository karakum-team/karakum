package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import io.github.sgrishchenko.karakum.extension.isBuiltin
import js.reflect.invoke
import typescript.isExpressionWithTypeArguments
import typescript.isHeritageClause
import typescript.isIdentifier
import typescript.isTypeReferenceNode

private val readonlyBuiltinTypeTypes: Map<String, String> = mapOf(
    "Array" to "js.array.ReadonlyArray",
    "Record" to "js.objects.ReadonlyRecord",
    "Map" to "js.collections.ReadonlyMap",
    "Set" to "js.collections.ReadonlySet",
)

private val builtinParentTypeMap: Map<String, String> = BROWSER_TYPES
    .associate { it.jsName to (it.kotlinMixinFqn ?: it.kotlinFqn) }
    // TODO: review
    .plus(readonlyBuiltinTypeTypes)

private val builtinTypeMap: Map<String, String> = BROWSER_TYPES
    .associate { it.jsName to it.kotlinFqn }
    .plus(readonlyBuiltinTypeTypes)

val convertBuiltinTypeReference = createPlugin plugin@{ node, context, _ ->
    if (!isIdentifier(node)) return@plugin null

    val parent = node.parent
    if (
        !isTypeReferenceNode(parent)
        && !isExpressionWithTypeArguments(parent)
    ) return@plugin null

    val jsName = node.text
    val useParentTypeMap = parent.parent
        .let { isHeritageClause(it) && it.token == typescript.SyntaxKind.ImplementsKeyword }
    val kotlinFqn = if (useParentTypeMap) {
        builtinParentTypeMap[jsName]
    } else {
        builtinTypeMap[jsName]
    } ?: return@plugin null

    if (!isBuiltin(node, context)) return@plugin null

    val checkCoverageService = context.lookupService(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    kotlinFqn
}

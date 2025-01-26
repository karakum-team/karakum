package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.Node
import typescript.isTypePredicateNode

val convertTypePredicate = createSimplePlugin plugin@{ node: Node, context, _ ->
    if (!isTypePredicateNode(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.deepCover(node)

    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    // TODO: support contracts

    val type = if (node.assertsModifier != null) "Unit" else "Boolean"

    "$type /* ${typeScriptService?.printNode(node)} */"
}

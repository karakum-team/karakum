package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import typescript.isTypePredicateNode

val convertTypePredicate = createPlugin plugin@{ node, context, _ ->
    if (!isTypePredicateNode(node)) return@plugin null

    val checkCoverageService = context.lookupService(checkCoverageServiceKey)
    checkCoverageService?.deepCover(node)

    val typeScriptService = context.lookupService(typeScriptServiceKey)

    // TODO: support contracts

    val type = if (node.assertsModifier != null) "Unit" else "Boolean"

    "$type /* ${typeScriptService?.printNode(node)} */"
}

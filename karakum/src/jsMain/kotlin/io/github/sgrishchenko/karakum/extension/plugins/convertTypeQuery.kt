package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import typescript.isTypeQueryNode

val convertTypeQuery = createPlugin plugin@{ node, context, render ->
    if (!isTypeQueryNode(node)) return@plugin null

    val checkCoverageService = context.lookupService(checkCoverageServiceKey)
    val typeScriptService = context.lookupService(typeScriptServiceKey)

    checkCoverageService?.deepCover(node)

    val resolvedType = typeScriptService?.resolveType(node)

    if (resolvedType == null || isTypeQueryNode(resolvedType)) {
        return@plugin "Any /* ${typeScriptService?.printNode(node)} */"
    }

    render(resolvedType)
}

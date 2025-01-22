package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.Node
import typescript.isIndexedAccessTypeNode

val convertIndexedAccessType = createSimplePlugin plugin@{ node: Node, context, render ->
    if (!isIndexedAccessTypeNode(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    checkCoverageService?.deepCover(node)

    val resolvedType = typeScriptService?.resolveType(node)

    if (resolvedType == null) return@plugin "Any /* ${typeScriptService?.printNode(node)} */";

    render(resolvedType)
}

package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.Node
import typescript.isLiteralTypeNode

val convertLiteralType = createSimplePlugin plugin@{ node: Node, context, render ->
    if (!isLiteralTypeNode(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    render(node.literal)
}

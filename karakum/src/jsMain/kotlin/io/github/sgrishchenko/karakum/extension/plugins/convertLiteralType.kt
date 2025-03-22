package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import typescript.isLiteralTypeNode

val convertLiteralType = createPlugin plugin@{ node, context, render ->
    if (!isLiteralTypeNode(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    render(node.literal)
}

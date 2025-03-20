package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.isParenthesizedTypeNode

val convertParenthesizedType = createSimplePlugin plugin@{ node, context, render ->
    if (!isParenthesizedTypeNode(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    "(${render(node.type)})"
}

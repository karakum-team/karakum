package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import typescript.isParenthesizedTypeNode

val convertParenthesizedType = createPlugin plugin@{ node, context, render ->
    if (!isParenthesizedTypeNode(node)) return@plugin null

    val checkCoverageService = context.lookupService(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    "(${render(node.type)})"
}

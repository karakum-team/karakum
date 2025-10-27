package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import typescript.isPropertyAccessExpression

val convertPropertyAccessExpression = createPlugin plugin@{ node, context, render ->
    if (!isPropertyAccessExpression(node)) return@plugin null

    val checkCoverageService = context.lookupService(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    "${render(node.expression)}.${render(node.name)}"
}

package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.Node
import typescript.isPropertyAccessExpression

val convertPropertyAccessExpression = createSimplePlugin plugin@{ node: Node, context, render ->
    if (!isPropertyAccessExpression(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    "${render(node.expression)}.${render(node.name)}"
}

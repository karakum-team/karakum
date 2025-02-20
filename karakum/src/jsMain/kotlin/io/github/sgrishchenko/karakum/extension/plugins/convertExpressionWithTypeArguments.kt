package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.Node
import typescript.isExpressionWithTypeArguments

val convertExpressionWithTypeArguments = createSimplePlugin plugin@{ node: Node, context, render ->
    if (!isExpressionWithTypeArguments(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    render(node.expression) + convertNodeWithTypeArguments.render(node, context, render)
}

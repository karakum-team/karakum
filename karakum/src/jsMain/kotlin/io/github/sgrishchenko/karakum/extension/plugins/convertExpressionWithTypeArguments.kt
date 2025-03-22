package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import typescript.isExpressionWithTypeArguments

val convertExpressionWithTypeArguments = createPlugin plugin@{ node, context, render ->
    if (!isExpressionWithTypeArguments(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    render(node.expression) + convertNodeWithTypeArguments(node, render)
}

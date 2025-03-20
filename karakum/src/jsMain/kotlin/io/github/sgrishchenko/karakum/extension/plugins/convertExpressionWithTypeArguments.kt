package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.isExpressionWithTypeArguments

val convertExpressionWithTypeArguments = createSimplePlugin plugin@{ node, context, render ->
    if (!isExpressionWithTypeArguments(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    render(node.expression) + convertNodeWithTypeArguments(node, render)
}

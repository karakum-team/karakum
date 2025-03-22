package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import typescript.isTypeReferenceNode

val convertTypeReference = createPlugin plugin@{ node, context, render ->
    if (!isTypeReferenceNode(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    render(node.typeName) + convertNodeWithTypeArguments(node, render)
}

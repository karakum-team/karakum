package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.Node
import typescript.isTypeReferenceNode

val convertTypeReference = createSimplePlugin plugin@{ node: Node, context, render ->
    if (!isTypeReferenceNode(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    render(node.typeName) + convertNodeWithTypeArguments.render(node, context, render)
}

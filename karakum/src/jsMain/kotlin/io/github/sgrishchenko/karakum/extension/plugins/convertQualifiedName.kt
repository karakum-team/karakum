package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import typescript.isQualifiedName

val convertQualifiedName = createPlugin plugin@{ node, context, render ->
    if (!isQualifiedName(node)) return@plugin null

    val checkCoverageService = context.lookupService(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    "${render(node.left)}.${render(node.right)}"
}

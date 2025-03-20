package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.isQualifiedName

val convertQualifiedName = createSimplePlugin plugin@{ node, context, render ->
    if (!isQualifiedName(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    "${render(node.left)}.${render(node.right)}"
}

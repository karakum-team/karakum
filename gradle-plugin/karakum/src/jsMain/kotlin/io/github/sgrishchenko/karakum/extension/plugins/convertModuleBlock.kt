package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.Node
import typescript.asArray
import typescript.isModuleBlock

val convertModuleBlock = createSimplePlugin plugin@{ node: Node, context, render ->
    if (!isModuleBlock(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    node.statements.asArray().joinToString(separator = "\n") { render(it) }
}

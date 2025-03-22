package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import typescript.asArray
import typescript.isModuleBlock

val convertModuleBlock = createPlugin plugin@{ node, context, render ->
    if (!isModuleBlock(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    node.statements.asArray().joinToString(separator = "\n") { render(it) }
}

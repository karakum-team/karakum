package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.isImportTypeNode

val convertImportType = createSimplePlugin plugin@{ node, context, render ->
    if (!isImportTypeNode(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    checkCoverageService?.cover(node)
    checkCoverageService?.deepCover(node.argument)

    val qualifier = node.qualifier?.let { render(it) } ?: "Any"

    "/* import(${typeScriptService?.printNode(node.argument)}) */ $qualifier"
}

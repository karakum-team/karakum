package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.Node
import typescript.SyntaxKind
import typescript.asArray
import typescript.isVariableStatement

val convertVariableStatement = createSimplePlugin plugin@{ node: Node, context, render ->
    if (!isVariableStatement(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)
    checkCoverageService?.cover(node.declarationList)

    val exportModifier = node.modifiers?.asArray()?.find { it.kind == SyntaxKind.ExportKeyword }
    exportModifier?.let { checkCoverageService?.cover(exportModifier) }

    node.declarationList.declarations.asArray().joinToString(separator = "\n") { render(it) }
}

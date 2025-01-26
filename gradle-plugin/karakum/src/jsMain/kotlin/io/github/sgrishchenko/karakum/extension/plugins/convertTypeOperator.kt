package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.Node
import typescript.SyntaxKind
import typescript.isTypeOperatorNode

val convertTypeOperator = createSimplePlugin plugin@{ node: Node, context, _ ->
    if (!isTypeOperatorNode(node)) return@plugin null

    if (
        node.operator == SyntaxKind.UniqueKeyword
        && node.type.kind == SyntaxKind.SymbolKeyword
    ) {
        val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
        checkCoverageService?.cover(node)
        checkCoverageService?.cover(node.type)

        return@plugin "/* unique */ js.symbol.Symbol"
    }

    null
}

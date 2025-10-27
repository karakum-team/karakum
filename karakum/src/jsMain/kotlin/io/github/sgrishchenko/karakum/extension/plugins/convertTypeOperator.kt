package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import typescript.SyntaxKind
import typescript.isTypeOperatorNode

val convertTypeOperator = createPlugin plugin@{ node, context, _ ->
    if (!isTypeOperatorNode(node)) return@plugin null

    if (
        node.operator == SyntaxKind.UniqueKeyword
        && node.type.kind == SyntaxKind.SymbolKeyword
    ) {
        val checkCoverageService = context.lookupService(checkCoverageServiceKey)
        checkCoverageService?.cover(node)
        checkCoverageService?.cover(node.type)

        return@plugin "/* unique */ js.symbol.Symbol"
    }

    null
}

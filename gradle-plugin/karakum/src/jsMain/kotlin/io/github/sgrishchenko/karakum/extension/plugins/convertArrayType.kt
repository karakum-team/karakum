package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.Node
import typescript.SyntaxKind
import typescript.isArrayTypeNode
import typescript.isTypeOperatorNode

val convertArrayType = createSimplePlugin plugin@{ node: Node, context, render ->
    if (
        isTypeOperatorNode(node) &&
        node.operator === SyntaxKind.ReadonlyKeyword
    ) {
        val type = node.type
        if (isArrayTypeNode(type)) {
            val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
            checkCoverageService?.cover(node)
            checkCoverageService?.cover(type)

            return@plugin "js.array.ReadonlyArray<${render(type.elementType)}>"
        }
    }

    if (isArrayTypeNode(node)) {
        val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
        checkCoverageService?.cover(node)

        return@plugin "js.array.ReadonlyArray<${render(node.elementType)}>"
    }

    null
}

package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import typescript.SyntaxKind
import typescript.isArrayTypeNode
import typescript.isTypeOperatorNode

val convertArrayType = createPlugin plugin@{ node, context, render ->
    if (
        isTypeOperatorNode(node) &&
        node.operator === SyntaxKind.ReadonlyKeyword
    ) {
        val type = node.type
        if (isArrayTypeNode(type)) {
            val checkCoverageService = context.lookupService(checkCoverageServiceKey)
            checkCoverageService?.cover(node)
            checkCoverageService?.cover(type)

            return@plugin "js.array.ReadonlyArray<${render(type.elementType)}>"
        }
    }

    if (isArrayTypeNode(node)) {
        val checkCoverageService = context.lookupService(checkCoverageServiceKey)
        checkCoverageService?.cover(node)

        return@plugin "js.array.ReadonlyArray<${render(node.elementType)}>"
    }

    null
}

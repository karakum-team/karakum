package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.ConverterPlugin
import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.Node

fun convertPrimitive(
    predicate: (node: Node) -> Boolean,
    render: (render: Node) -> String?,
): ConverterPlugin<Node> {
    return createSimplePlugin plugin@{ node: Node, context, _ ->
        if (!predicate(node)) return@plugin null

        val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
        checkCoverageService?.cover(node)

        render(node)
    }
}

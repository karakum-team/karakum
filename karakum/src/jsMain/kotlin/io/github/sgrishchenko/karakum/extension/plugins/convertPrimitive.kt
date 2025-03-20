package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.Plugin
import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.Node

fun convertPrimitive(
    predicate: (node: Node) -> Boolean,
    render: (render: Node) -> String?,
): Plugin {
    return createSimplePlugin plugin@{ node, context, _ ->
        if (!predicate(node)) return@plugin null

        val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
        checkCoverageService?.cover(node)

        render(node)
    }
}

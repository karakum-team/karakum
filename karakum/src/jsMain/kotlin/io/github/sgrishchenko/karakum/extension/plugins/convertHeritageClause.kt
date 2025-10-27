package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import typescript.asArray
import typescript.isHeritageClause

val convertHeritageClause = createPlugin plugin@{ node, context, render ->
    if (!isHeritageClause(node)) return@plugin null

    val checkCoverageService = context.lookupService(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    node.types.asArray()
        .map { render(it) }
        .filter { it.isNotEmpty() }
        .joinToString(separator = ", ")
}

package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.asArray
import typescript.isHeritageClause

val convertHeritageClause = createSimplePlugin plugin@{ node, context, render ->
    if (!isHeritageClause(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    node.types.asArray()
        .map { render(it) }
        .filter { it.isNotEmpty() }
        .joinToString(separator = ", ")
}

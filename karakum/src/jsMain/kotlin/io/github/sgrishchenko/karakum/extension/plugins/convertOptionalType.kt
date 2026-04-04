package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import io.github.sgrishchenko.karakum.extension.renderNullable
import typescript.isOptionalTypeNode

val convertOptionalType = createPlugin plugin@{ node, context, render ->
    if (!isOptionalTypeNode(node)) return@plugin null

    val checkCoverageService = context.lookupService(checkCoverageServiceKey)

    checkCoverageService?.cover(node)

    renderNullable(node.type, isNullable = true, context, render)
}

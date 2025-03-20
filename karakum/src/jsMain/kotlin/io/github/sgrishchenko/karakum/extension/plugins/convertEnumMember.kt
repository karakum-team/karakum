package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.isEnumMember

val convertEnumMember = createSimplePlugin plugin@{ node, context, render ->
    if (!isEnumMember(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    // skip initializer
    node.initializer?.let { checkCoverageService?.cover(it) }

    "val ${render(node.name)}"
}

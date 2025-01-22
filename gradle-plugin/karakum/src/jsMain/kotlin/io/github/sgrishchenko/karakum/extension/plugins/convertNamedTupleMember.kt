package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.Node
import typescript.isNamedTupleMember

val convertNamedTupleMember = createSimplePlugin plugin@{ node: Node, context, render ->
    if (!isNamedTupleMember(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)

    checkCoverageService?.cover(node)

    val name = render(node.name)
    val type = render(node.type)

    "/* ${name}: */ $type"
}

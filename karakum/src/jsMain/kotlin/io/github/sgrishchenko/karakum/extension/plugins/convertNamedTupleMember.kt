package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import io.github.sgrishchenko.karakum.extension.renderNullable
import typescript.isNamedTupleMember

val convertNamedTupleMember = createPlugin plugin@{ node, context, render ->
    if (!isNamedTupleMember(node)) return@plugin null

    val checkCoverageService = context.lookupService(checkCoverageServiceKey)

    checkCoverageService?.cover(node)

    node.questionToken?.let { checkCoverageService?.cover(it) }

    val isOptional = node.questionToken != null

    val name = render(node.name)
    val type = renderNullable(node.type, isOptional, context, render)

    "/* ${name}: */ $type"
}

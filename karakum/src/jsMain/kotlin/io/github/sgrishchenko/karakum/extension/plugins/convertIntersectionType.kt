package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import typescript.isIntersectionTypeNode

val convertIntersectionType = createPlugin plugin@{ node, context, _ ->
    if (!isIntersectionTypeNode(node)) return@plugin null

    val typeScriptService = context.lookupService(typeScriptServiceKey)

    "Any /* ${typeScriptService?.printNode(node)} */"
}

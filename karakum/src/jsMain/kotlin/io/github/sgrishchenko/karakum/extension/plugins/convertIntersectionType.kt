package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.isIntersectionTypeNode

val convertIntersectionType = createSimplePlugin plugin@{ node, context, _ ->
    if (!isIntersectionTypeNode(node)) return@plugin null

    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    "Any /* ${typeScriptService?.printNode(node)} */"
}

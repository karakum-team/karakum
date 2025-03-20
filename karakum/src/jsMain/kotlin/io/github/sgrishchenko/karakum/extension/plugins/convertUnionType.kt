package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.isUnionTypeNode

val convertUnionType = createSimplePlugin plugin@{ node, context, _ ->
    if (!isUnionTypeNode(node)) return@plugin null

    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    val isNullable = isPossiblyNullableType(node, context)

    "Any${if (isNullable) "?" else ""} /* ${typeScriptService?.printNode(node)} */"
}

package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import js.reflect.invoke
import typescript.isUnionTypeNode

val convertUnionType = createPlugin plugin@{ node, context, _ ->
    if (!isUnionTypeNode(node)) return@plugin null

    val typeScriptService = context.lookupService(typeScriptServiceKey)

    val isNullable = isPossiblyNullableType(node, context)

    "Any${if (isNullable) "?" else ""} /* ${typeScriptService?.printNode(node)} */"
}

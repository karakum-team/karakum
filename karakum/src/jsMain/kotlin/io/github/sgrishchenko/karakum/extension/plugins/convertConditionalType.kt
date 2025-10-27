package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import typescript.isConditionalTypeNode

val convertConditionalType = createPlugin plugin@{ node, context, _ ->
    if (!isConditionalTypeNode(node)) return@plugin null

    val typeScriptService = context.lookupService(typeScriptServiceKey)

    val isNullableTrue = isPossiblyNullableType(node.trueType, context)
    val isNullableFalse = isPossiblyNullableType(node.falseType, context)
    val isNullable = isNullableTrue || isNullableFalse

    "Any${if (isNullable) "?" else ""} /* ${typeScriptService?.printNode(node)} */"
}

package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.Node
import typescript.isConditionalTypeNode

val convertConditionalType = createSimplePlugin plugin@{ node: Node, context, _ ->
    if (!isConditionalTypeNode(node)) return@plugin null

    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    val isNullableTrue = isPossiblyNullableType(node.trueType, context)
    val isNullableFalse = isPossiblyNullableType(node.falseType, context)
    val isNullable = isNullableTrue || isNullableFalse

    "Any${if (isNullable) "?" else ""} /* ${typeScriptService?.printNode(node)} */"
}

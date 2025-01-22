package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.TypeScriptService
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.Node
import typescript.isFunctionDeclaration

val resolveFunctionReturnTypeName = NameResolver<Node> { node, context ->
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriptService?.getParent(it) ?: it.getParentOrNull()
    }

    val functionNode = getParent(node)
        ?: return@NameResolver null
    if (!isFunctionDeclaration(functionNode)) return@NameResolver null

    val functionNameNode = functionNode.name
        ?: return@NameResolver null
    if (functionNode.type != node) return@NameResolver null

    val parentName = functionNameNode.text

    "${capitalize(parentName)}Result"
}

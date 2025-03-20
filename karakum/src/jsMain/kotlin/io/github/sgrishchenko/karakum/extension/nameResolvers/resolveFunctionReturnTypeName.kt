package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.TypeScriptService
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.Node
import typescript.isFunctionDeclaration

val resolveFunctionReturnTypeName: NameResolver = nameResolver@{ node, context ->
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriptService?.getParent(it) ?: it.getParentOrNull()
    }

    val functionNode = getParent(node)
        ?: return@nameResolver null
    if (!isFunctionDeclaration(functionNode)) return@nameResolver null

    val functionNameNode = functionNode.name
        ?: return@nameResolver null
    if (functionNode.type != node) return@nameResolver null

    val parentName = functionNameNode.text

    "${capitalize(parentName)}Result"
}

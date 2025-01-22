package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.TypeScriptService
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.Node
import typescript.isFunctionDeclaration
import typescript.isIdentifier
import typescript.isParameter

val resolveFunctionParameterName = NameResolver<Node> { node, context ->
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriptService?.getParent(it) ?: it.getParentOrNull()
    }

    val parameter = getParent(node)
        ?: return@NameResolver null
    if (!isParameter(parameter)) return@NameResolver null

    val parameterNameNode = parameter.name
    if (!isIdentifier(parameterNameNode)) return@NameResolver null

    val parameterName = parameterNameNode.text

    val functionNode = getParent(parameter)
        ?: return@NameResolver null
    if (!isFunctionDeclaration(functionNode)) return@NameResolver null

    val functionNameNode = functionNode.name
        ?: return@NameResolver null

    val parentName = functionNameNode.text

    "${capitalize(parentName)}${capitalize(parameterName)}"
}

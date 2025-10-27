package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.TypeScriptService
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.*

val resolveFunctionTypeAliasParameterName: NameResolver = nameResolver@{ node, context ->
    val typeScriptService = context.lookupService(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriptService?.getParent(it) ?: it.getParentOrNull()
    }

    val parameter = getParent(node)
        ?: return@nameResolver null
    if (!isParameter(parameter)) return@nameResolver null

    val parameterNameNode = parameter.name
    if (!isIdentifier(parameterNameNode)) return@nameResolver null

    val parameterName = parameterNameNode.text

    val functionType = getParent(parameter)
        ?: return@nameResolver null
    if (!isFunctionTypeNode(functionType)) return@nameResolver null

    val typeAlias = getParent(functionType)
        ?: return@nameResolver null
    if (!isTypeAliasDeclaration(typeAlias)) return@nameResolver null

    val parentName = typeAlias.name.text

    "${capitalize(parentName)}${capitalize(parameterName)}"
}

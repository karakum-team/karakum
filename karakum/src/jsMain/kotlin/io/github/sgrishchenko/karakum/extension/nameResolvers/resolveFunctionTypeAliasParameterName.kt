package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.TypeScriptService
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.*

val resolveFunctionTypeAliasParameterName = NameResolver { node, context ->
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

    val functionType = getParent(parameter)
        ?: return@NameResolver null
    if (!isFunctionTypeNode(functionType)) return@NameResolver null

    val typeAlias = getParent(functionType)
        ?: return@NameResolver null
    if (!isTypeAliasDeclaration(typeAlias)) return@NameResolver null

    val parentName = typeAlias.name.text

    "${capitalize(parentName)}${capitalize(parameterName)}"
}

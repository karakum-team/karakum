package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.TypeScriptService
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.*

val resolveConstructorParameterName = NameResolver<Node> { node, context ->
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

    val valructor = getParent(parameter)
        ?: return@NameResolver null
    if (!isConstructorDeclaration(valructor)) return@NameResolver null

    val classNode = getParent(valructor)
        ?: return@NameResolver null
    if (!isClassDeclaration(classNode)) return@NameResolver null

    val classNameNode = classNode.name
        ?: return@NameResolver null

    val parentName = classNameNode.text

    "${capitalize(parentName)}${capitalize(parameterName)}"
}

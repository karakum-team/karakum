package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.TypeScriptService
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.*

val resolveClassMethodParameterName: NameResolver = nameResolver@{ node, context ->
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriptService?.getParent(it) ?: it.getParentOrNull()
    }

    val parameter = getParent(node)
        ?: return@nameResolver null
    if (!isParameter(parameter)) return@nameResolver null
    val parameterNameNode = parameter.name
    if (!isIdentifier(parameterNameNode)) return@nameResolver null

    val parameterName = parameterNameNode.text

    val method = getParent(parameter)
        ?: return@nameResolver null
    if (!isMethodDeclaration(method)) return@nameResolver null

    val methodNameNode = method.name
    if (!isIdentifier(methodNameNode)) return@nameResolver null

    val methodName = methodNameNode.text

    val classNode = getParent(method)
        ?: return@nameResolver null
    if (!isClassDeclaration(classNode)) return@nameResolver null
    val classNameNode = classNode.name
        ?: return@nameResolver null

    val parentName = classNameNode.text

    "${capitalize(parentName)}${capitalize(methodName)}${capitalize(parameterName)}"
}

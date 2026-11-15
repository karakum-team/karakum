package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import js.reflect.invoke
import typescript.ast.Node
import typescript.ast.`is`.isIdentifier
import typescript.ast.`is`.isInterfaceDeclaration
import typescript.ast.`is`.isMethodSignature
import typescript.ast.`is`.isParameter

val resolveInterfaceMethodParameterName: NameResolver = nameResolver@{ node, context ->
    val typeScriptService = context.lookupService(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriptService?.getParent(it) ?: it.parent
    }

    val parameter = getParent(node)
        ?: return@nameResolver null
    if (!isParameter(parameter)) return@nameResolver null

    val parameterNameNode = parameter.name
    if (!isIdentifier(parameterNameNode)) return@nameResolver null

    val parameterName = parameterNameNode.text

    val method = getParent(parameter)
        ?: return@nameResolver null
    if (!isMethodSignature(method)) return@nameResolver null

    val methodNameNode = method.name
    if (!isIdentifier(methodNameNode)) return@nameResolver null

    val methodName = methodNameNode.text

    val interfaceNode = getParent(method)
        ?: return@nameResolver null
    if (!isInterfaceDeclaration(interfaceNode)) return@nameResolver null

    val parentName = interfaceNode.name.text

    "${capitalize(parentName)}${capitalize(methodName)}${capitalize(parameterName)}"
}

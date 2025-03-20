package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.TypeScriptService
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.*

val resolveInterfaceMethodCallbackParameterName: NameResolver = nameResolver@{ node, context ->
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriptService?.getParent(it) ?: it.getParentOrNull()
    }

    val callbackParameter = getParent(node)
        ?: return@nameResolver null
    if (!isParameter(callbackParameter)) return@nameResolver null

    val cbParameterNameNode = callbackParameter.name
    if (!isIdentifier(cbParameterNameNode)) return@nameResolver null

    val callbackParameterName = cbParameterNameNode.text

    val functionType = getParent(callbackParameter)
        ?: return@nameResolver null
    if (!isFunctionTypeNode(functionType)) return@nameResolver null

    val parameter = getParent(functionType)
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

    "${capitalize(parentName)}${capitalize(methodName)}${capitalize(parameterName)}${capitalize(callbackParameterName)}"
}

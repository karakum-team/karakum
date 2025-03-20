package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.TypeScriptService
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.*

val resolveInterfaceMethodCallbackParameterName = NameResolver { node, context ->
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriptService?.getParent(it) ?: it.getParentOrNull()
    }

    val callbackParameter = getParent(node)
        ?: return@NameResolver null
    if (!isParameter(callbackParameter)) return@NameResolver null

    val cbParameterNameNode = callbackParameter.name
    if (!isIdentifier(cbParameterNameNode)) return@NameResolver null

    val callbackParameterName = cbParameterNameNode.text

    val functionType = getParent(callbackParameter)
        ?: return@NameResolver null
    if (!isFunctionTypeNode(functionType)) return@NameResolver null

    val parameter = getParent(functionType)
        ?: return@NameResolver null
    if (!isParameter(parameter)) return@NameResolver null

    val parameterNameNode = parameter.name
    if (!isIdentifier(parameterNameNode)) return@NameResolver null

    val parameterName = parameterNameNode.text

    val method = getParent(parameter)
        ?: return@NameResolver null
    if (!isMethodSignature(method)) return@NameResolver null

    val methodNameNode = method.name
    if (!isIdentifier(methodNameNode)) return@NameResolver null

    val methodName = methodNameNode.text

    val interfaceNode = getParent(method)
        ?: return@NameResolver null
    if (!isInterfaceDeclaration(interfaceNode)) return@NameResolver null

    val parentName = interfaceNode.name.text

    "${capitalize(parentName)}${capitalize(methodName)}${capitalize(parameterName)}${capitalize(callbackParameterName)}"
}

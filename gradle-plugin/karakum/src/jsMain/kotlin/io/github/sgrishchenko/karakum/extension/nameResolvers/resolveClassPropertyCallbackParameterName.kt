package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.TypeScriptService
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.*

val resolveClassPropertyCallbackParameterName = NameResolver<Node> { node, context ->
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

    val property = getParent(functionType)
        ?: return@NameResolver null
    if (!isPropertyDeclaration(property)) return@NameResolver null

    val propertyNameNode = property.name
    if (!isIdentifier(propertyNameNode)) return@NameResolver null

    val propertyName = propertyNameNode.text

    val classNode = getParent(property)
        ?: return@NameResolver null
    if (!isClassDeclaration(classNode)) return@NameResolver null

    val classNameNode = classNode.name
        ?: return@NameResolver null

    val parentName = classNameNode.text

    "${capitalize(parentName)}${capitalize(propertyName)}${capitalize(callbackParameterName)}"
}

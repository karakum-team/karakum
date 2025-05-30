package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.TypeScriptService
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.*

val resolveInterfacePropertyCallbackParameterName: NameResolver = nameResolver@{ node, context ->
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

    val property = getParent(functionType)
        ?: return@nameResolver null
    if (!isPropertySignature(property)) return@nameResolver null

    val propertyNameNode = property.name
    if (!isIdentifier(propertyNameNode)) return@nameResolver null

    val propertyName = propertyNameNode.text

    val interfaceNode = getParent(property)
        ?: return@nameResolver null
    if (!isInterfaceDeclaration(interfaceNode)) return@nameResolver null

    val parentName = interfaceNode.name.text

    "${capitalize(parentName)}${capitalize(propertyName)}${capitalize(callbackParameterName)}"
}

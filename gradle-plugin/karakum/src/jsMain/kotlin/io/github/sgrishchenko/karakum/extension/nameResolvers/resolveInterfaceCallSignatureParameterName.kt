package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.TypeScriptService
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.*

val resolveInterfaceCallSignatureParameterName = NameResolver<Node> { node, context ->
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

    val callSignature = getParent(parameter)
        ?: return@NameResolver null
    if (!isCallSignatureDeclaration(callSignature)) return@NameResolver null

    val interfaceNode = getParent(callSignature)
        ?: return@NameResolver null
    if (!isInterfaceDeclaration(interfaceNode)) return@NameResolver null

    val parentName = interfaceNode.name.text

    "${capitalize(parentName)}${capitalize(parameterName)}"
}

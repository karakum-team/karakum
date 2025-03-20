package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.TypeScriptService
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.Node
import typescript.isIdentifier
import typescript.isInterfaceDeclaration
import typescript.isMethodSignature

val resolveInterfaceMethodReturnTypeName = NameResolver { node, context ->
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriptService?.getParent(it) ?: it.getParentOrNull()
    }

    val method = getParent(node)
        ?: return@NameResolver null
    if (!isMethodSignature(method)) return@NameResolver null

    val methodNameNode = method.name
    if (!isIdentifier(methodNameNode)) return@NameResolver null
    if (method.type != node) return@NameResolver null

    val methodName = methodNameNode.text

    val interfaceNode = getParent(method)
        ?: return@NameResolver null
    if (!isInterfaceDeclaration(interfaceNode)) return@NameResolver null

    val parentName = interfaceNode.name.text

    "${capitalize(parentName)}${capitalize(methodName)}Result"
}

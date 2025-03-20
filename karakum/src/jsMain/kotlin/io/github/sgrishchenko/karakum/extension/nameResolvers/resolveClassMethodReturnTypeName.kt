package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.TypeScriptService
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.Node
import typescript.isClassDeclaration
import typescript.isIdentifier
import typescript.isMethodDeclaration

val resolveClassMethodReturnTypeName = NameResolver { node, context ->
    val typeScriprvice = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriprvice?.getParent(it) ?: it.getParentOrNull()
    }

    val method = getParent(node)
        ?: return@NameResolver null
    if (!isMethodDeclaration(method)) return@NameResolver null
    val methodNameNode = method.name
    if (!isIdentifier(methodNameNode)) return@NameResolver null
    if (method.type !== node) return@NameResolver null

    val methodName = methodNameNode.text

    val classNode = getParent(method)
        ?: return@NameResolver null
    if (!isClassDeclaration(classNode)) return@NameResolver null
    val classNameNode = classNode.name
        ?: return@NameResolver null

    val parentName = classNameNode.text

    "${capitalize(parentName)}${capitalize(methodName)}Result"
}

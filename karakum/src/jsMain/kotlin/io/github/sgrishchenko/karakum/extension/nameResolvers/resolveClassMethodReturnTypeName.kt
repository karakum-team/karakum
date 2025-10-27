package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.Node
import typescript.isClassDeclaration
import typescript.isIdentifier
import typescript.isMethodDeclaration

val resolveClassMethodReturnTypeName: NameResolver = nameResolver@{ node, context ->
    val typeScriptService = context.lookupService(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriptService?.getParent(it) ?: it.getParentOrNull()
    }

    val method = getParent(node)
        ?: return@nameResolver null
    if (!isMethodDeclaration(method)) return@nameResolver null
    val methodNameNode = method.name
    if (!isIdentifier(methodNameNode)) return@nameResolver null
    if (method.type !== node) return@nameResolver null

    val methodName = methodNameNode.text

    val classNode = getParent(method)
        ?: return@nameResolver null
    if (!isClassDeclaration(classNode)) return@nameResolver null
    val classNameNode = classNode.name
        ?: return@nameResolver null

    val parentName = classNameNode.text

    "${capitalize(parentName)}${capitalize(methodName)}Result"
}

package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import js.reflect.invoke
import typescript.ast.Node
import typescript.ast.`is`.isIdentifier
import typescript.ast.`is`.isInterfaceDeclaration
import typescript.ast.`is`.isMethodSignature

val resolveInterfaceMethodReturnTypeName: NameResolver = nameResolver@{ node, context ->
    val typeScriptService = context.lookupService(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriptService?.getParent(it) ?: it.parent
    }

    val method = getParent(node)
        ?: return@nameResolver null
    if (!isMethodSignature(method)) return@nameResolver null

    val methodNameNode = method.name
    if (!isIdentifier(methodNameNode)) return@nameResolver null
    if (method.type != node) return@nameResolver null

    val methodName = methodNameNode.text

    val interfaceNode = getParent(method)
        ?: return@nameResolver null
    if (!isInterfaceDeclaration(interfaceNode)) return@nameResolver null

    val parentName = interfaceNode.name.text

    "${capitalize(parentName)}${capitalize(methodName)}Result"
}

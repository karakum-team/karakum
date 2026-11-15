package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import js.reflect.invoke
import typescript.ast.Node
import typescript.ast.`is`.isIdentifier
import typescript.ast.`is`.isInterfaceDeclaration
import typescript.ast.`is`.isPropertySignature

val resolveInterfacePropertyName: NameResolver = nameResolver@{ node, context ->
    val typeScriptService = context.lookupService(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriptService?.getParent(it) ?: it.parent
    }

    val property = getParent(node)
        ?: return@nameResolver null
    if (!isPropertySignature(property)) return@nameResolver null

    val propertyNameNode = property.name
    if (!isIdentifier(propertyNameNode)) return@nameResolver null

    val propertyName = propertyNameNode.text

    val interfaceNode = getParent(property)
        ?: return@nameResolver null
    if (!isInterfaceDeclaration(interfaceNode)) return@nameResolver null

    val parentName = interfaceNode.name.text

    "${capitalize(parentName)}${capitalize(propertyName)}"
}

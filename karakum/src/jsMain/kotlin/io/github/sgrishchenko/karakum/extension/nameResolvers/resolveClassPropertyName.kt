package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.TypeScriptService
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.Node
import typescript.isClassDeclaration
import typescript.isIdentifier
import typescript.isPropertyDeclaration

val resolveClassPropertyName = NameResolver { node, context ->
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriptService?.getParent(it) ?: it.getParentOrNull()
    }

    val property = getParent(node)
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

    "${capitalize(parentName)}${capitalize(propertyName)}"
}

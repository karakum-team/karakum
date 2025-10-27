package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.Node
import typescript.isClassDeclaration
import typescript.isIdentifier
import typescript.isPropertyDeclaration

val resolveClassPropertyName: NameResolver = nameResolver@{ node, context ->
    val typeScriptService = context.lookupService(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriptService?.getParent(it) ?: it.getParentOrNull()
    }

    val property = getParent(node)
        ?: return@nameResolver null
    if (!isPropertyDeclaration(property)) return@nameResolver null

    val propertyNameNode = property.name
    if (!isIdentifier(propertyNameNode)) return@nameResolver null

    val propertyName = propertyNameNode.text

    val classNode = getParent(property)
        ?: return@nameResolver null
    if (!isClassDeclaration(classNode)) return@nameResolver null

    val classNameNode = classNode.name
        ?: return@nameResolver null

    val parentName = classNameNode.text

    "${capitalize(parentName)}${capitalize(propertyName)}"
}

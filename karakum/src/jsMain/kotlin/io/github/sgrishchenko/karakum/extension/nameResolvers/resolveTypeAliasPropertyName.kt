package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.TypeScriptService
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.*

val resolveTypeAliasPropertyName: NameResolver = nameResolver@{ node, context ->
    val typeScriptService = context.lookupService(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriptService?.getParent(it) ?: it.getParentOrNull()
    }

    val property = getParent(node)
        ?: return@nameResolver null
    if (!isPropertySignature(property)) return@nameResolver null

    val propertyNameNode = property.name
    if (!isIdentifier(propertyNameNode)) return@nameResolver null

    val propertyName = propertyNameNode.text

    val typeLiteral = getParent(property)
        ?: return@nameResolver null
    if (!isTypeLiteralNode(typeLiteral)) return@nameResolver null

    val typeAlias = getParent(typeLiteral)
        ?: return@nameResolver null
    if (!isTypeAliasDeclaration(typeAlias)) return@nameResolver null

    val parentName = typeAlias.name.text

    "${capitalize(parentName)}${capitalize(propertyName)}"
}

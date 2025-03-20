package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.extension.plugins.TypeScriptService
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import io.github.sgrishchenko.karakum.util.capitalize
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.*

val resolveTypeAliasPropertyName = NameResolver { node, context ->
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriptService?.getParent(it) ?: it.getParentOrNull()
    }

    val property = getParent(node)
        ?: return@NameResolver null
    if (!isPropertySignature(property)) return@NameResolver null

    val propertyNameNode = property.name
    if (!isIdentifier(propertyNameNode)) return@NameResolver null

    val propertyName = propertyNameNode.text

    val typeLiteral = getParent(property)
        ?: return@NameResolver null
    if (!isTypeLiteralNode(typeLiteral)) return@NameResolver null

    val typeAlias = getParent(typeLiteral)
        ?: return@NameResolver null
    if (!isTypeAliasDeclaration(typeAlias)) return@NameResolver null

    val parentName = typeAlias.name.text

    "${capitalize(parentName)}${capitalize(propertyName)}"
}

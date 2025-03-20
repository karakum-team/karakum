package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.Node
import typescript.isLiteralTypeNode
import typescript.isStringLiteral
import typescript.isTypeAliasDeclaration
import typescript.isUnionTypeNode

val resolveUnionMemberDuplicateName = NameResolver { node, context ->
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriptService?.getParent(it) ?: it.getParentOrNull()
    }

    if (!isLiteralTypeNode(node)) return@NameResolver null
    if (!isStringLiteral(node.literal)) return@NameResolver null

    val unionType = getParent(node)
        ?: return@NameResolver null
    if (!isUnionTypeNode(unionType)) return@NameResolver null

    val typeAlias = getParent(unionType)
        ?: return@NameResolver null
    if (!isTypeAliasDeclaration(typeAlias)) return@NameResolver null

    if (typeAlias.name.text !== "UnionWithDuplicates") return@NameResolver "stringTrue"

    null
}

package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.NameResolver
import io.github.sgrishchenko.karakum.util.getParentOrNull
import typescript.*

val resolveUnionMemberDuplicateName: NameResolver = nameResolver@{ node, context ->
    val typeScriptService = context.lookupService(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriptService?.getParent(it) ?: it.getParentOrNull()
    }

    if (!isLiteralTypeNode(node)) return@nameResolver null
    if (!isStringLiteral(node.literal)) return@nameResolver null

    val unionType = getParent(node)
        ?: return@nameResolver null
    if (!isUnionTypeNode(unionType)) return@nameResolver null

    val typeAlias = getParent(unionType)
        ?: return@nameResolver null
    if (!isTypeAliasDeclaration(typeAlias)) return@nameResolver null

    if (typeAlias.name.text !== "UnionWithDuplicates") return@nameResolver "stringTrue"

    null
}

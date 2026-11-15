package io.github.sgrishchenko.karakum.extension.nameResolvers

import io.github.sgrishchenko.karakum.extension.Context
import io.github.sgrishchenko.karakum.extension.plugins.typeScriptServiceKey
import js.reflect.invoke
import typescript.ast.Node
import typescript.ast.`is`.isLiteralTypeNode
import typescript.ast.`is`.isStringLiteral
import typescript.ast.`is`.isTypeAliasDeclaration
import typescript.ast.`is`.isUnionTypeNode

suspend fun resolveUnionMemberDuplicateName(node: Node, context: Context): String? {
    val typeScriptService = context.lookupService(typeScriptServiceKey)
    val getParent = { it: Node ->
        typeScriptService?.getParent(it) ?: it.parent
    }

    if (!isLiteralTypeNode(node)) return null
    if (!isStringLiteral(node.literal)) return null

    val unionType = getParent(node)
        ?: return null
    if (!isUnionTypeNode(unionType)) return null

    val typeAlias = getParent(unionType)
        ?: return null
    if (!isTypeAliasDeclaration(typeAlias)) return null

    if (typeAlias.name.text != "UnionWithDuplicates") return "stringTrue"

    return null
}

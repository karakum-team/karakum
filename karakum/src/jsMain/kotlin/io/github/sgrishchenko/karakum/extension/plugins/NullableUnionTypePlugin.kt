package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.*
import io.github.sgrishchenko.karakum.util.getSourceFileOrNull
import io.github.sgrishchenko.karakum.util.resolveParenthesizedType
import js.array.ReadonlyArray
import typescript.*
import kotlin.contracts.contract

private fun isNull(type: Node) = isLiteralTypeNode(type) && type.literal.kind === SyntaxKind.NullKeyword
private fun isUndefined(type: Node) = type.kind === SyntaxKind.UndefinedKeyword
private fun isAny(type: Node) = type.kind === SyntaxKind.AnyKeyword
private fun isUnknown(type: Node) = type.kind === SyntaxKind.UnknownKeyword

fun isNullableType(type: Node) = isNull(type) || isUndefined(type)

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
fun isPossiblyNullableType(node: TypeNode, context: Context): Boolean {
    if (node.getSourceFileOrNull() == null) {
        // handle synthetic nodes
        val resolvedType = resolveTypeIfNeeded(node, context)

        return (
            isNullableTsNode(resolvedType)
            || (
                isUnionTypeNode(resolvedType)
                && flatUnionTypes(resolvedType, context).any { isNullableTsNode(it) }
            )
        )
    }

    val typeScriptService = context.lookupService(typeScriptServiceKey)

    val typeChecker = typeScriptService?.program?.getTypeChecker()

    val type = typeChecker?.getTypeAtLocation(node) ?: return false

    return (
        isNullableTsType(type)
        || (
            type.isUnion()
            && (type as UnionType).types.any { isNullableTsType(it) }
        )
    )
}

private fun resolveTypeIfNeeded(node: TypeNode, context: Context): Node {
    val typeScriptService = context.lookupService(typeScriptServiceKey)

    if (isIndexedAccessTypeNode(node)) {
        val resolvedType = typeScriptService?.resolveType(node)

        return resolvedType ?: node
    }

    return node
}

private fun isNullableTsNode(node: Node): Boolean {
    return (
        isNull(node)
        || isUndefined(node)
        || isAny(node)
        || isUnknown(node)
    )
}

private fun isNullableTsType(type: Type): Boolean {
    // TODO: provide bit mask for TypeFlags
    return (
        (type.flags.toString().toInt() and TypeFlags.Null.toString().toInt()) != 0
        || (type.flags.toString().toInt() and TypeFlags.Undefined.toString().toInt()) != 0
        || (type.flags.toString().toInt() and TypeFlags.Any.toString().toInt()) != 0
        || (type.flags.toString().toInt() and TypeFlags.Unknown.toString().toInt()) != 0
    )
}

@Suppress("CANNOT_CHECK_FOR_EXTERNAL_INTERFACE")
fun isNullableUnionType(node: Node, context: Context): Boolean {
    contract {
        returns(true) implies (node is UnionTypeNode)
    }

    if (!isUnionTypeNode(node)) return false

    return flatUnionTypes(node, context).any { isNullableType(it) }
}

@Suppress("CANNOT_CHECK_FOR_EXTERNAL_INTERFACE")
fun isNullableOnlyUnionType(node: Node, context: Context): Boolean {
    contract {
        returns(true) implies (node is UnionTypeNode)
    }

    if (!isUnionTypeNode(node)) return false

    return flatUnionTypes(node, context).all { isNullableType(it) }
}

fun flatUnionTypes(node: UnionTypeNode, context: Context): ReadonlyArray<TypeNode> {
    val result = mutableListOf<TypeNode>()

    for (type in node.types) {
        if (isIndexedAccessTypeNode(type)) {
            val typeScriptService = context.lookupService(typeScriptServiceKey)

            val resolvedType = typeScriptService?.resolveType(type)

            if (resolvedType != null && isUnionTypeNode(resolvedType)) {
                result += flatUnionTypes(resolvedType, context).toList()
            } else {
                result += type
            }
        } else if (isParenthesizedTypeNode(type)) {
            val resolvedType = resolveParenthesizedType(type)

            if (isUnionTypeNode(resolvedType)) {
                result += flatUnionTypes(resolvedType, context).toList()
            } else {
                result += resolvedType
            }
        } else {
            result += type
        }
    }

    return result.toTypedArray()
}

class NullableUnionTypePlugin : Plugin {
    override fun generate(context: Context, render: Render<Node>) = emptyArray<GeneratedFile>()

    override fun render(node: Node, context: Context, next: Render<Node>): String? {
        val checkCoverageService = context.lookupService(checkCoverageServiceKey)
        checkCoverageService?.cover(node)

        if (isNullableOnlyUnionType(node, context)) {
            node.types.asArray().forEach { checkCoverageService?.deepCover(it) }

            return "Nothing?"
        }

        if (isNullableLiteralUnionType(node, context)) return null

        if (isNullableUnionType(node, context)) {
            val types = flatUnionTypes(node, context)

            val nonNullableTypes = types.filter{ !isNullableType(it) }
            val nullableTypes = node.types.asArray().filter { isNullableType(it) }

            nullableTypes.forEach { checkCoverageService?.deepCover(it) }

            if (nonNullableTypes.size == 1) {
                val nonNullableType = nonNullableTypes[0]

                // any and unknown case
                if (isPossiblyNullableType(nonNullableType, context)) {
                    return next(nonNullableType)
                }

                return renderResolvedNullable(nonNullableType, true, next)
            }
        }

        return null
    }

    override fun setup(context: Context) = Unit

    override fun traverse(node: Node, context: Context) = Unit
}

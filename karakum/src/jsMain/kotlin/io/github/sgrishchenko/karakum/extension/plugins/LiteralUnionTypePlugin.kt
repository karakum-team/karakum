package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.configuration.`object`
import io.github.sgrishchenko.karakum.extension.Context
import io.github.sgrishchenko.karakum.extension.HERITAGE_CLAUSE
import io.github.sgrishchenko.karakum.extension.InjectionType
import io.github.sgrishchenko.karakum.extension.Render
import io.github.sgrishchenko.karakum.extension.ifPresent
import io.github.sgrishchenko.karakum.util.escapeIdentifier
import js.reflect.unsafeCast
import kotlinx.js.JsPlainObject
import typescript.*
import kotlin.contracts.contract

sealed external interface LiteralUnionMemberEntryType {
    companion object
}

inline val LiteralUnionMemberEntryType.Companion.string: LiteralUnionMemberEntryType
    get() = unsafeCast("string")

inline val LiteralUnionMemberEntryType.Companion.boolean: LiteralUnionMemberEntryType
    get() = unsafeCast("boolean")

inline val LiteralUnionMemberEntryType.Companion.number: LiteralUnionMemberEntryType
    get() = unsafeCast("number")

inline val LiteralUnionMemberEntryType.Companion.bigInt: LiteralUnionMemberEntryType
    get() = unsafeCast("bigInt")

@JsPlainObject
external interface LiteralUnionMemberEntry {
    val key: String
    val value: String
    val type: LiteralUnionMemberEntryType
}

@JsPlainObject
external interface LiteralUnionRenderResult {
    val declaration: String
    val generated: String
    val nullable: Boolean
}

private fun extractBooleanUnionMemberEntry(node: LiteralTypeNode): LiteralUnionMemberEntry? {
    if (node.literal.kind === SyntaxKind.TrueKeyword) {
        return LiteralUnionMemberEntry("`true`", "true", LiteralUnionMemberEntryType.boolean)
    }
    if (node.literal.kind === SyntaxKind.FalseKeyword) {
        return LiteralUnionMemberEntry("`false`", "false", LiteralUnionMemberEntryType.boolean)
    }
    return null
}

private fun extractStringUnionMemberEntry(node: LiteralTypeNode): LiteralUnionMemberEntry? {
    val literal = node.literal
    if (isStringLiteral(literal)) {
        return LiteralUnionMemberEntry(
            convertMemberNameLiteral(literal),
            literal.text,
            LiteralUnionMemberEntryType.string,
        )
    }
    return null
}

private fun extractNumericUnionMemberEntry(node: LiteralTypeNode): LiteralUnionMemberEntry? {
    val literal = node.literal
    if (isNumericLiteral(literal)) {
        return LiteralUnionMemberEntry("`${literal.text}`", literal.text, LiteralUnionMemberEntryType.number)
    }
    if (isBigIntLiteral(literal)) {
        return LiteralUnionMemberEntry("`${literal.text}`", literal.text, LiteralUnionMemberEntryType.bigInt)
    }
    return null
}

private fun extractPrefixUnaryExpressionUnionMemberEntry(node: LiteralTypeNode): LiteralUnionMemberEntry? {
    val literal = node.literal
    if (
        isPrefixUnaryExpression(literal)
        && literal.operator == SyntaxKind.MinusToken
    ) {
        val operand = literal.operand
        if (isNumericLiteral(operand)) {
            return LiteralUnionMemberEntry("`-${operand.text}`", "-${operand.text}", LiteralUnionMemberEntryType.number)
        }
        if (isBigIntLiteral(operand)) {
            return LiteralUnionMemberEntry("`-${operand.text}`", "-${operand.text}", LiteralUnionMemberEntryType.bigInt)
        }
    }
    return null
}

private fun extractUnionMemberEntry(node: LiteralTypeNode): LiteralUnionMemberEntry? {
    return extractBooleanUnionMemberEntry(node)
        ?: extractStringUnionMemberEntry(node)
        ?: extractNumericUnionMemberEntry(node)
        ?: extractPrefixUnaryExpressionUnionMemberEntry(node)
}

private fun resolveUnionMemberEntry(node: LiteralTypeNode, context: Context): LiteralUnionMemberEntry {
    val nameResolverService = context.requireService(nameResolverServiceKey)

    val entry = extractUnionMemberEntry(node) ?: error("Unsupported literal type")

    val resolvedName = nameResolverService.tryResolveName(node, context)
    if (resolvedName != null) {
        return LiteralUnionMemberEntry.copy(entry, key = resolvedName)
    }

    return LiteralUnionMemberEntry.copy(entry, escapeIdentifier(entry.key))
}

private fun isNegativeNumericLikeLiteralType(node: LiteralTypeNode): Boolean {
    val literal = node.literal
    return isPrefixUnaryExpression(literal)
            && literal.operator == SyntaxKind.MinusToken
            && (
                isNumericLiteral(literal.operand)
                || isBigIntLiteral(literal.operand)
            )
}

@Suppress("CANNOT_CHECK_FOR_EXTERNAL_INTERFACE")
private fun isSupportedLiteralType(node: Node): Boolean {
    contract {
        returns(true) implies (node is LiteralTypeNode)
    }

    return isLiteralTypeNode(node) && (
        node.literal.kind === SyntaxKind.TrueKeyword
        || node.literal.kind === SyntaxKind.FalseKeyword

        || isStringLiteral(node.literal)

        || isNumericLiteral(node.literal)
        || isBigIntLiteral(node.literal)

        || isNegativeNumericLikeLiteralType(node)
    )
}

@Suppress("CANNOT_CHECK_FOR_EXTERNAL_INTERFACE")
fun isLiteralUnionType(node: Node, context: Context): Boolean {
    contract {
        returns(true) implies (node is UnionTypeNode)
    }

    return (
        isUnionTypeNode(node)
        && flatUnionTypes(node, context).all(::isSupportedLiteralType)
    )
}

@Suppress("CANNOT_CHECK_FOR_EXTERNAL_INTERFACE")
fun isNullableLiteralUnionType(node: Node, context: Context): Boolean {
    contract {
        returns(true) implies (node is UnionTypeNode)
    }

    if (!isUnionTypeNode(node)) return false

    val types = flatUnionTypes(node, context)
    val nonNullableTypes = types.filter { !isNullableType(it) }

    return (
        types.all {
            isNullableType(it)
                    || isSupportedLiteralType(it)
        }
        && nonNullableTypes.size > 1
    )
}

fun convertLiteralUnionType(
    node: UnionTypeNode,
    name: String,
    qualifiedName: String,
    isInlined: Boolean,
    context: Context,
    render: Render<Node>,
): LiteralUnionRenderResult {
    val checkCoverageService = context.lookupService(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    val typeScriptService = context.lookupService(typeScriptServiceKey)
    val namespaceInfoService = context.lookupService(namespaceInfoServiceKey)
    val injectionService = context.lookupService(injectionServiceKey)

    val types = flatUnionTypes(node, context)

    val nonNullableTypes = types.filter { !isNullableType(it) }
    val nullableTypes = types.filter { isNullableType(it) }

    val entries: List<LiteralUnionMemberEntry> = nonNullableTypes
        .mapNotNull {
            if (!isSupportedLiteralType(it)) return@mapNotNull null

            checkCoverageService?.deepCover(it)

            resolveUnionMemberEntry(it, context)
        }
        // other entries should have priority over strings
        .sortedBy { it.type == LiteralUnionMemberEntryType.string }

    val existingKeys = mutableListOf<String>()
    val uniqueEntries = mutableListOf<LiteralUnionMemberEntry>()
    val duplicatedEntries = mutableListOf<LiteralUnionMemberEntry>()

    for (entry in entries) {
        if (entry.key !in existingKeys) {
            uniqueEntries += entry
            existingKeys += entry.key
        } else {
            duplicatedEntries += entry
        }
    }

    val values = uniqueEntries.joinToString(separator = "\n\n") { entry ->
        when (entry.type) {
            LiteralUnionMemberEntryType.string -> {
                """
inline val ${qualifiedName}.Companion.${entry.key}: $qualifiedName
    get() = js.reflect.unsafeCast("${entry.value}")
                """.trim()
            }
            LiteralUnionMemberEntryType.bigInt -> {
                """
inline val ${qualifiedName}.Companion.${entry.key}: $qualifiedName
    get() = js.reflect.unsafeCast(js.core.BigInt("${entry.value.removeSuffix("n")}"))
                """.trim()
            }
            else -> {
                """
inline val ${qualifiedName}.Companion.${entry.key}: $qualifiedName
    get() = js.reflect.unsafeCast(${entry.value})
                """.trim()
            }
        }
    }

    val comment = duplicatedEntries.joinToString(separator = "\n") { entry ->
        if (entry.type == LiteralUnionMemberEntryType.string) {
            "${entry.key} for \"${entry.value}\""
        } else {
            "${entry.key} for ${entry.value}"
        }
    }

    val heritageInjections = injectionService?.resolveInjections(node, InjectionType.HERITAGE_CLAUSE, context, render)

    val namespace = typeScriptService?.findClosestNamespace(node)

    var externalModifier = "external "

    if (
        isInlined
        && namespace != null
        && namespaceInfoService?.resolveNamespaceStrategy(namespace) == NamespaceStrategy.`object`
    ) {
        externalModifier = ""
    }

    val injectedHeritageClauses = heritageInjections
        ?.filter { it.isNotEmpty() }
        ?.joinToString(separator = ", ")

    val declaration = """
sealed ${externalModifier}interface ${name}${ifPresent(injectedHeritageClauses) { " : $it" }} {
companion object
}
    """.trim()

    val generated = """
        ${values}${ifPresent(comment) {
            "\n" + """
/*
Duplicated names were generated:
$it
*/
            """.trim() + "\n"
        }}
    """.trim()

    val nullable = nullableTypes.isNotEmpty()

    return LiteralUnionRenderResult(
        declaration = declaration,
        generated = generated,
        nullable = nullable,
    )
}

fun createLiteralUnionTypePlugin() = createAnonymousDeclarationPlugin plugin@{ node, context, render ->
    if (!isNullableLiteralUnionType(node, context)) return@plugin null

    val name = context.resolveName(node)
    val qualifiedName = name

    val result = convertLiteralUnionType(node, name, qualifiedName, false, context, render)
    val declaration = result.declaration
    val generated = result.generated
    val nullable = result.nullable

    val reference = if (nullable) "${name}?" else name

    AnonymousDeclaration(
        name = name,
        declaration = "$declaration\n\n$generated",
        reference = reference,
    )
}

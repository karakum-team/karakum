package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.extension.ConverterContext
import io.github.sgrishchenko.karakum.extension.InjectionType
import io.github.sgrishchenko.karakum.extension.Render
import io.github.sgrishchenko.karakum.extension.ifPresent
import io.github.sgrishchenko.karakum.util.escapeIdentifier
import js.objects.JsPlainObject
import typescript.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@JsPlainObject
external interface LiteralUnionMemberEntry {
    val key: String
    val value: String
    val isString: Boolean
}

@JsPlainObject
external interface LiteralUnionRenderResult {
    val declaration: String
    val nullable: Boolean
}

private fun extractBooleanUnionMemberName(node: LiteralTypeNode): String? {
    if (node.literal.kind === SyntaxKind.TrueKeyword) return "`true`"
    if (node.literal.kind === SyntaxKind.FalseKeyword) return "`false`"
    return null
}

private fun extractStringUnionMemberName(node: LiteralTypeNode): String? {
    val literal = node.literal
    if (isStringLiteral(literal)) return convertMemberNameLiteral(literal)
    return null
}

private fun extractNumericUnionMemberName(node: LiteralTypeNode): String? {
    val literal = node.literal
    if (isNumericLiteral(literal)) return "`${literal.text}`"
    if (isBigIntLiteral(literal)) return "`${literal.text}`"
    return null
}

private fun extractPrefixUnaryExpressionUnionMemberName(node: LiteralTypeNode): String? {
    val literal = node.literal
    if (
        isPrefixUnaryExpression(literal)
        && literal.operator == SyntaxKind.MinusToken
    ) {
        val operand = literal.operand
        if (isNumericLiteral(operand)) return "`-${operand.text}`"
        if (isBigIntLiteral(operand)) return "`-${operand.text}`"
    }
    return null
}

private fun extractUnionMemberName(node: LiteralTypeNode): String? {
    return extractBooleanUnionMemberName(node)
        ?: extractStringUnionMemberName(node)
        ?: extractNumericUnionMemberName(node)
        ?: extractPrefixUnaryExpressionUnionMemberName(node)
}

private fun resolveUnionMemberName(node: LiteralTypeNode, context: ConverterContext): String {
    val nameResolverService = context.lookupService<NameResolverService>(nameResolverServiceKey)
    if (nameResolverService == null) error("AnonymousDeclarationPlugin can't work without NameResolverService")

    val resolvedName = nameResolverService.tryResolveName(node, context)
    if (resolvedName) return resolvedName

    val extractedName = extractUnionMemberName(node)
    if (extractedName == null) error("Unsupported literal type")

    return escapeIdentifier(extractedName)
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

@OptIn(ExperimentalContracts::class)
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

@OptIn(ExperimentalContracts::class)
@Suppress("CANNOT_CHECK_FOR_EXTERNAL_INTERFACE")
fun isLiteralUnionType(node: Node, context: ConverterContext): Boolean {
    contract {
        returns(true) implies (node is UnionTypeNode)
    }

    return (
        isUnionTypeNode(node)
        && flatUnionTypes(node, context).all(::isSupportedLiteralType)
    )
}

@OptIn(ExperimentalContracts::class)
@Suppress("CANNOT_CHECK_FOR_EXTERNAL_INTERFACE")
fun isNullableLiteralUnionType(node: Node, context: ConverterContext): Boolean {
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
    isInlined: Boolean,
    context: ConverterContext,
    render: Render<Node>,
): LiteralUnionRenderResult {
    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    val namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)
    val injectionService = context.lookupService<InjectionService>(injectionServiceKey)

    val types = flatUnionTypes(node, context)

    val nonNullableTypes = types.filter { !isNullableType(it) }
    val nullableTypes = types.filter { isNullableType(it) }

    val stringEntries: List<LiteralUnionMemberEntry> = nonNullableTypes
        .mapNotNull {
            if (!isSupportedLiteralType(it)) return@mapNotNull null

            val literal = it.literal
            if (!isStringLiteral(literal)) return@mapNotNull null

            checkCoverageService?.deepCover(it)

            LiteralUnionMemberEntry(
                key = resolveUnionMemberName(it, context),
                value = literal.text,
                isString = true,
            )
        }

    val otherEntries: List<LiteralUnionMemberEntry> = nonNullableTypes
        .mapNotNull {
            if (!isSupportedLiteralType(it)) return@mapNotNull null

            val literal = it.literal
            if (isStringLiteral(literal)) return@mapNotNull null

            checkCoverageService?.deepCover(it)

            val value = typeScriptService?.printNode(it)
            if (value == null) error("Unsupported literal type")

            LiteralUnionMemberEntry(
                key = resolveUnionMemberName(it, context),
                value = value,
                isString = false,
            )
        }

    val existingKeys = mutableListOf<String>()
    val uniqueEntries = mutableListOf<LiteralUnionMemberEntry>()
    val duplicatedEntries = mutableListOf<LiteralUnionMemberEntry>()

    // other entries should have priority over strings
    for (entry in otherEntries + stringEntries) {
        if (entry.key !in existingKeys) {
            uniqueEntries += entry
            existingKeys += entry.key
        } else {
            duplicatedEntries += entry
        }
    }

    val body = uniqueEntries.joinToString(separator = "\n") { entry ->
        if (entry.isString) {
            """
@seskar.js.JsValue("${entry.value}")
val ${entry.key}: ${name}
            """.trim()
        } else {
            """
@seskar.js.JsRawValue("${entry.value}")
val ${entry.key}: ${name}
            """.trim()
        }
    }

    val comment = duplicatedEntries.joinToString(separator = "\n") { entry ->
        if (entry.isString) {
            "${entry.key} for \"${entry.value}\""
        } else {
            "${entry.key} for ${entry.value}"
        }
    }

    val heritageInjections = injectionService?.resolveInjections(node, InjectionType.HERITAGE_CLAUSE, context, render)

    val namespace = typeScriptService?.findClosest(node, ::isModuleDeclaration)

    var externalModifier = "external "

    if (
        isInlined
        && namespace != null
        && isModuleDeclaration(namespace)
        && namespaceInfoService?.resolveNamespaceStrategy(namespace) == NamespaceStrategy.`object`
    ) {
        externalModifier = ""
    }

    val injectedHeritageClauses = heritageInjections
        ?.filter { it.isNotEmpty() }
        ?.joinToString(separator = ", ")

    val declaration = """
sealed ${externalModifier}interface ${name}${ifPresent(injectedHeritageClauses) { " : $it" }} {
companion object {
${body}${ifPresent(comment) {
    "\n" + """
/*
Duplicated names were generated:
$it
*/
    """.trim()
}}
}
}
    """.trim()

    val nullable = nullableTypes.isNotEmpty()

    return LiteralUnionRenderResult(
        declaration = declaration,
        nullable = nullable,
    )
}

fun literalUnionTypePlugin = createAnonymousDeclarationPlugin(
    (node, context, render) => {
        if (!isNullableLiteralUnionType(node, context)) return null

        const name = context.resolveName(node)

        const {declaration, nullable} = convertLiteralUnionType(node, name, false, context, render)

        const reference = nullable ? `${name}?` : name

        return {name, declaration, reference};
    }
)

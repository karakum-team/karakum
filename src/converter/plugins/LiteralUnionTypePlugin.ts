import ts, {LiteralTypeNode, UnionTypeNode} from "typescript";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {ConverterContext} from "../context.js";
import {createAnonymousDeclarationPlugin} from "./AnonymousDeclarationPlugin.js";
import {flatUnionTypes, isNullableType} from "./NullableUnionTypePlugin.js";
import {ifPresent, Render} from "../render.js";
import {InjectionService, injectionServiceKey} from "./InjectionPlugin.js";
import {InjectionType} from "../injection.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";
import {NamespaceInfoService, namespaceInfoServiceKey} from "./NamespaceInfoPlugin.js";
import {convertMemberNameLiteral} from "./convertMemberName.js";
import {escapeIdentifier} from "../../utils/strings.js";

type LiteralUnionMemberEntry = {
    key: string
    value: string
    isString: boolean
}

function extractBooleanUnionMemberName(node: LiteralTypeNode): string | null {
    if (node.literal.kind === ts.SyntaxKind.TrueKeyword) return "`true`"
    if (node.literal.kind === ts.SyntaxKind.FalseKeyword) return "`false`"
    return null
}

function extractStringUnionMemberName(node: LiteralTypeNode): string | null {
    if (ts.isStringLiteral(node.literal)) return convertMemberNameLiteral(node.literal)
    return null
}

function extractNumericUnionMemberName(node: LiteralTypeNode): string | null {
    if (
        ts.isNumericLiteral(node.literal)
        || ts.isBigIntLiteral(node.literal)
    ) {
        return `\`${node.literal.text}\``
    }
    return null
}

function extractPrefixUnaryExpressionUnionMemberName(node: LiteralTypeNode): string | null {
    if (
        ts.isPrefixUnaryExpression(node.literal)
        && node.literal.operator === ts.SyntaxKind.MinusToken
        && (
            ts.isNumericLiteral(node.literal.operand)
            || ts.isBigIntLiteral(node.literal.operand)
        )
    ) {
        return `\`-${node.literal.operand.text}\``
    }
    return null
}

function extractUnionMemberName(node: LiteralTypeNode): string | null {
    return extractBooleanUnionMemberName(node)
        ?? extractStringUnionMemberName(node)
        ?? extractNumericUnionMemberName(node)
        ?? extractPrefixUnaryExpressionUnionMemberName(node)
}

function isSupportedLiteralType(node: ts.Node): node is LiteralTypeNode {
    return ts.isLiteralTypeNode(node) && (
        node.literal.kind === ts.SyntaxKind.TrueKeyword
        || node.literal.kind === ts.SyntaxKind.FalseKeyword

        || ts.isStringLiteral(node.literal)

        || ts.isNumericLiteral(node.literal)
        || ts.isBigIntLiteral(node.literal)

        || (
            ts.isPrefixUnaryExpression(node.literal)
            && node.literal.operator === ts.SyntaxKind.MinusToken
            && (
                ts.isNumericLiteral(node.literal.operand)
                || ts.isBigIntLiteral(node.literal.operand)
            )
        )
    )
}

export function isLiteralUnionType(node: ts.Node, context: ConverterContext): node is UnionTypeNode {
    return (
        ts.isUnionTypeNode(node)
        && flatUnionTypes(node, context).every(isSupportedLiteralType)
    )
}

export function isNullableLiteralUnionType(node: ts.Node, context: ConverterContext): node is UnionTypeNode {
    if (!ts.isUnionTypeNode(node)) return false

    const types = flatUnionTypes(node, context)
    const nonNullableTypes = types.filter(type => !isNullableType(type))

    return (
        types.every(type => (
            isNullableType(type)
            || isSupportedLiteralType(type)
        ))
        && nonNullableTypes.length > 1
    )
}

export function convertLiteralUnionType(
    node: UnionTypeNode,
    name: string,
    isInlined: boolean,
    context: ConverterContext,
    render: Render,
) {
    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    const namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)
    const injectionService = context.lookupService<InjectionService>(injectionServiceKey)

    const types = flatUnionTypes(node, context)

    const nonNullableTypes = types.filter(type => !isNullableType(type))
    const nullableTypes = types.filter(type => isNullableType(type))

    const stringEntries = nonNullableTypes
        .filter(isSupportedLiteralType)
        .filter(type=> ts.isStringLiteral(type.literal))
        .map(type => {
            checkCoverageService?.deepCover(type)

            const literal = type.literal
            if (!ts.isStringLiteral(literal)) throw new Error("Unsupported literal type")

            const memberName = extractUnionMemberName(type)
            if (!memberName) throw new Error("Unsupported literal type")

            const key = escapeIdentifier(memberName)
            return {
                key,
                value: literal.text,
                isString: true,
            }
        })

    const otherEntries = nonNullableTypes
        .filter(isSupportedLiteralType)
        .filter(type => !ts.isStringLiteral(type.literal))
        .map(type => {
            checkCoverageService?.deepCover(type)

            const value = typeScriptService?.printNode(type)
            if (!value) throw new Error("Unsupported literal type")

            const memberName = extractUnionMemberName(type)
            if (!memberName) throw new Error("Unsupported literal type")

            const key = escapeIdentifier(memberName)
            return {
                key,
                value: value,
                isString: false,
            }
        })

    const existingKeys = new Set<string>()
    const uniqueEntries: LiteralUnionMemberEntry[] = []
    const duplicatedEntries: LiteralUnionMemberEntry[] = []

    // other entries should have priority over strings
    for (const entry of [...otherEntries, ...stringEntries]) {
        if (!existingKeys.has(entry.key)) {
            uniqueEntries.push(entry)
            existingKeys.add(entry.key)
        } else {
            duplicatedEntries.push(entry)
        }
    }

    const body = uniqueEntries
        .map(entry => {
            if (entry.isString) {
                return `
@seskar.js.JsValue("${entry.value}")
val ${entry.key}: ${name}
                `.trim()
            } else {
                return `
@seskar.js.JsRawValue("${entry.value}")
val ${entry.key}: ${name}
            `.trim()
            }
        })
        .join("\n")

    const comment = duplicatedEntries
        .map(entry => {
            if (entry.isString) {
                return `${entry.key} for "${entry.value}"`
            } else {
                return `${entry.key} for ${entry.value}`
            }
        })
        .join("\n")

    const heritageInjections = injectionService?.resolveInjections(node, InjectionType.HERITAGE_CLAUSE, context, render)

    const namespace = typeScriptService?.findClosest(node, ts.isModuleDeclaration)

    let externalModifier = "external "

    if (isInlined && namespace !== undefined && namespaceInfoService?.resolveNamespaceStrategy(namespace) === "object") {
        externalModifier = ""
    }

    const injectedHeritageClauses = heritageInjections
        ?.filter(Boolean)
        ?.join(", ")

    const declaration = `
sealed ${externalModifier}interface ${name}${ifPresent(injectedHeritageClauses, it => ` : ${it}`)} {
companion object {
${body}${ifPresent(comment, it => (
    "\n" + `
/*
Duplicated names were generated:
${it}
*/
    `.trim()
))}
}
}
    `.trim()

    const nullable = nullableTypes.length > 0

    return {
        declaration,
        nullable,
    }
}

export const literalUnionTypePlugin = createAnonymousDeclarationPlugin(
    (node, context, render) => {
        if (!isNullableLiteralUnionType(node, context)) return null

        const name = context.resolveName(node)

        const {declaration, nullable} = convertLiteralUnionType(node, name, false, context, render)

        const reference = nullable ? `${name}?` : name

        return {name, declaration, reference};
    }
)

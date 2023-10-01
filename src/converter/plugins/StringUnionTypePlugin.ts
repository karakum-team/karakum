import ts, {LiteralTypeNode, StringLiteral, UnionTypeNode} from "typescript";
import {escapeIdentifier, identifier} from "../../utils/strings.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {ConverterContext} from "../context.js";
import {createAnonymousDeclarationPlugin} from "./AnonymousDeclarationPlugin.js";
import {flatUnionTypes, isNullableType, isNullableOnlyUnionType} from "./NullableUnionTypePlugin.js";

export function isStringUnionType(node: ts.Node, context: ConverterContext): node is UnionTypeNode {
    return (
        ts.isUnionTypeNode(node)
        && flatUnionTypes(node, context).every(type => (
            ts.isLiteralTypeNode(type)
            && ts.isStringLiteral(type.literal)
        ))
    )
}

export function isNullableStringUnionType(node: ts.Node, context: ConverterContext): node is UnionTypeNode {
    return (
        ts.isUnionTypeNode(node)
        && flatUnionTypes(node, context).every(type => (
            isNullableType(type)
            || (
                ts.isLiteralTypeNode(type)
                && ts.isStringLiteral(type.literal)
            )
        ))
    )
}

export function convertStringUnionType(node: UnionTypeNode, name: string, context: ConverterContext) {
    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const types = flatUnionTypes(node, context)

    const nonNullableTypes = types.filter(type => !isNullableType(type))
    const nullableTypes = types.filter(type => isNullableType(type))

    const entries = nonNullableTypes
        .filter((type): type is LiteralTypeNode => ts.isLiteralTypeNode(type))
        .map(type => {
            checkCoverageService?.cover(type)

            return type.literal
        })
        .filter((literal): literal is StringLiteral => ts.isStringLiteral(literal))
        .map(literal => {
            checkCoverageService?.cover(literal)

            const value = literal.text
            const key = identifier(value)
            return [key, value] as const
        })

    const keys = entries.map(([key]) => escapeIdentifier(key))
    const uniqueKeys = Array.from(new Set(keys))

    const body = uniqueKeys
        .map(key => `val ${key}: ${name}`)
        .join("\n")

    const jsName = entries
        .map(([key, value]) => `${key}: '${value}'`)
        .join(", ")

    const declaration = `
@Suppress(
    "NAME_CONTAINS_ILLEGAL_CHARS",
    "NESTED_CLASS_IN_EXTERNAL_INTERFACE",
)
@JsName("""(/*union*/{${jsName}}/*union*/)""")
sealed external interface ${name} {
companion object {
${body}
}
}
    `

    const nullable = nullableTypes.length > 0

    return {
        declaration,
        nullable,
    }
}

export const stringUnionTypePlugin = createAnonymousDeclarationPlugin(
    (node, context) => {
        if (isNullableOnlyUnionType(node, context)) return null
        if (!isNullableStringUnionType(node, context)) return null

        const name = context.resolveName(node)

        const {declaration, nullable} = convertStringUnionType(node, name, context)

        const reference = nullable ? `${name}?` : name

        return {name, declaration, reference};
    }
)

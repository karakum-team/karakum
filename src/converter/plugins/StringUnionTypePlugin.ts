import ts, {LiteralTypeNode, StringLiteral, UnionTypeNode} from "typescript";
import {escapeIdentifier, identifier} from "../../utils/strings";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {ConverterContext} from "../context";
import {createAnonymousDeclarationPlugin} from "./AnonymousDeclarationPlugin";
import {defaultNameResolvers} from "../defaultNameResolvers";

export function isStringUnionType(node: ts.Node): node is UnionTypeNode {
    return (
        ts.isUnionTypeNode(node)
        && node.types.every(type => (
            ts.isLiteralTypeNode(type)
            && ts.isStringLiteral(type.literal)
        ))
    )
}

export const convertStringUnionType = (node: UnionTypeNode, name: string, context: ConverterContext) => {
    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const entries = node.types
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

    return `
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
};

export const createStringUnionTypePlugin = createAnonymousDeclarationPlugin(
    defaultNameResolvers,
    (node, context) => {
        if (!isStringUnionType(node)) return null

        const name = context.resolveName(node)

        const declaration = convertStringUnionType(node, name, context)

        const reference = name

        return {name, declaration, reference};
    }
)

import ts, {Node, TypeNode, UnionTypeNode} from "typescript";
import {ConverterPlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {ConverterContext} from "../context.js";
import {Render, renderResolvedNullable} from "../render.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";
import {resolveParenthesizedType} from "../../utils/resolveParenthesizedType.js";
import {isNullableStringUnionType} from "./StringUnionTypePlugin.js";
import {GeneratedFile} from "../generated.js";

const isNull = (type: Node) => ts.isLiteralTypeNode(type) && type.literal.kind === ts.SyntaxKind.NullKeyword
const isUndefined = (type: Node) => type.kind === ts.SyntaxKind.UndefinedKeyword
const isUnknown = (type: Node) => type.kind === ts.SyntaxKind.UnknownKeyword
const isAny = (type: Node) => type.kind === ts.SyntaxKind.AnyKeyword

export const isNullableType = (type: Node) => isNull(type) || isUndefined(type)

export const isPossiblyNullableType = (type: Node, context: ConverterContext) => (
    isNullableType(type)
    || isUnknown(type)
    || isAny(type)
    || isNullableUnionType(type, context)
)

export function isNullableUnionType(node: Node, context: ConverterContext): node is UnionTypeNode {
    if (!ts.isUnionTypeNode(node)) return false

    return flatUnionTypes(node, context).some(type => isNullableType(type))
}

export function isNullableOnlyUnionType(node: Node, context: ConverterContext): node is UnionTypeNode {
    if (!ts.isUnionTypeNode(node)) return false

    return flatUnionTypes(node, context).every(type => isNullableType(type))
}

export function flatUnionTypes(node: UnionTypeNode, context: ConverterContext) {
    let result: TypeNode[] = []

    for (const type of node.types) {
        if (ts.isIndexedAccessTypeNode(type)) {
            const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

            const resolvedType = typeScriptService?.resolveType(type)

            if (resolvedType && ts.isUnionTypeNode(resolvedType)) {
                result = result.concat(flatUnionTypes(resolvedType, context))
            } else {
                result.push(type)
            }
        } else if (ts.isParenthesizedTypeNode(type)) {
            const resolvedType = resolveParenthesizedType(type)

            if (ts.isUnionTypeNode(resolvedType)) {
                result = result.concat(flatUnionTypes(resolvedType, context))
            } else {
                result.push(resolvedType)
            }
        } else {
            result.push(type)
        }
    }

    return result
}

export class NullableUnionTypePlugin implements ConverterPlugin {
    generate(): GeneratedFile[] {
        return [];
    }

    render(node: ts.Node, context: ConverterContext, next: Render): string | null {
        const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
        checkCoverageService?.cover(node)

        if (isNullableOnlyUnionType(node, context)) {
            node.types.forEach(it => checkCoverageService?.deepCover(it))

            return "Nothing?"
        }

        if (isNullableStringUnionType(node, context)) return null

        if (isNullableUnionType(node, context)) {
            const types = flatUnionTypes(node, context)

            const nonNullableTypes = types.filter(type => !isNullableType(type))
            const nullableTypes = node.types.filter(type => isNullableType(type))

            nullableTypes.forEach(it => checkCoverageService?.deepCover(it))

            if (nonNullableTypes.length === 1) {
                const nonNullableType = nonNullableTypes[0]
                return renderResolvedNullable(nonNullableType, true, next)
            } else {
                return `(${next(node)})?`
            }
        }

        return null;
    }

    setup(context: ConverterContext): void {
    }

    traverse(node: ts.Node, context: ConverterContext): void {
    }
}

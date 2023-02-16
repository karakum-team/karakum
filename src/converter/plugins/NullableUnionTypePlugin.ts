import ts, {Node, SyntaxKind, UnionTypeNode} from "typescript";
import {ConverterPlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {ConverterContext} from "../context";
import {Render} from "../render";

const isNull = (type: Node) => ts.isLiteralTypeNode(type) && type.literal.kind === SyntaxKind.NullKeyword
const isUndefined = (type: Node) => type.kind === SyntaxKind.UndefinedKeyword
const isUnknown = (type: Node) => type.kind === SyntaxKind.UnknownKeyword
const isAny = (type: Node) => type.kind === SyntaxKind.AnyKeyword

export const isNullableType = (type: Node) => isNull(type) || isUndefined(type)

export const isPossiblyNullableType = (type: Node) => (
    isNullableType(type)
    || isUnknown(type)
    || isAny(type)
    || isNullableUnionType(type)
)

export function isNullableUnionType(node: Node): node is UnionTypeNode {
    return (
        ts.isUnionTypeNode(node)
        && node.types.some(type => isNullableType(type))
    )
}

export class NullableUnionTypePlugin implements ConverterPlugin {
    private coveredUnion = new Set<Node>()

    generate(context: ConverterContext): Record<string, string> {
        return {};
    }

    render(node: ts.Node, context: ConverterContext, next: Render): string | null {
        if (this.coveredUnion.has(node)) return null
        this.coveredUnion.add(node)

        if (isNullableUnionType(node)) {

            const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
            checkCoverageService?.cover(node)

            const nonNullableTypes = node.types.filter(type => !isNullableType(type))
            const nullableTypes = node.types.filter(type => isNullableType(type))

            nullableTypes.forEach(it => checkCoverageService?.deepCover(it))

            if (nonNullableTypes.length === 1 && !ts.isFunctionTypeNode(nonNullableTypes[0])) {
                const nonNullableType = nonNullableTypes[0]
                return `${next(nonNullableType)}?`
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

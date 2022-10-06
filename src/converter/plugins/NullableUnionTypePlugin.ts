import ts, {Node, SyntaxKind, UnionTypeNode} from "typescript";
import {ConverterPlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {ConverterContext} from "../context";
import {Render} from "../render";

const isNull = (type: Node) => ts.isLiteralTypeNode(type) && type.literal.kind === SyntaxKind.NullKeyword
const isUndefined = (type: Node) => type.kind === SyntaxKind.UndefinedKeyword

export const isNullableType = (type: Node) => isNull(type) || isUndefined(type)

export function isNullableUnionType(node: UnionTypeNode) {
    return node.types.some(type => isNullableType(type))
}

export class NullableUnionTypePlugin implements ConverterPlugin {
    private coveredUnion = new Set<Node>()

    generate(context: ConverterContext): Record<string, string> {
        return {};
    }

    render(node: ts.Node, context: ConverterContext, next: Render): string | null {
        if (this.coveredUnion.has(node)) return null
        this.coveredUnion.add(node)

        if (ts.isUnionTypeNode(node) && isNullableUnionType(node)) {

            const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
            checkCoverageService?.cover(node)

            const nonNullableTypes = node.types.filter(type => !isNullableType(type))
            const nullableType = node.types.find(type => isNullableType(type))

            if (nullableType) {
                checkCoverageService?.cover(nullableType)
            }

            nullableType && checkCoverageService?.deepCover(nullableType)

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

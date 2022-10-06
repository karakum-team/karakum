import ts, {Node, SyntaxKind, UnionTypeNode} from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

const isNull = (type: Node) => ts.isLiteralTypeNode(type) && type.literal.kind === SyntaxKind.NullKeyword
const isUndefined = (type: Node) => type.kind === SyntaxKind.UndefinedKeyword

export const isNullableType = (type: Node) => isNull(type) || isUndefined(type)

export function isNullableUnionType(node: UnionTypeNode) {
    return node.types.some(type => isNullableType(type))
}


export const convertNullableUnionType = createSimplePlugin((node, context, render) => {
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
            return `${render(nonNullableType)}?`
        } else {
            const generatedNode = ts.factory.createUnionTypeNode(nonNullableTypes)
            return `(${render(generatedNode)})?`
        }
    }

    return null;
})

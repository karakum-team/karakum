import ts, {Node, SyntaxKind} from "typescript";
import {ConverterPlugin} from "../plugin";

const isNull = (type: Node) => ts.isLiteralTypeNode(type) && type.literal.kind === SyntaxKind.NullKeyword
const isUndefined = (type: Node) => type.kind === SyntaxKind.UndefinedKeyword

const isNullable = (type: Node) => isNull(type) || isUndefined(type)

export const convertOptionalUnionType: ConverterPlugin = (node, context, render) => {
    if (ts.isUnionTypeNode(node) &&
        node.types.length === 2 &&
        node.types.some(type => isNullable(type))
    ) {
        context.cover(node)
        context.cover(node.types[0])
        context.cover(node.types[1])

        const nonNullableType = node.types.find(type => !isNullable(type))
        const nullableType = node.types.find(type => isNullable(type))

        nullableType && context.deepCover(nullableType)

        return (nonNullableType && `${render(nonNullableType)}?`) ?? ""
    }

    return null;
}

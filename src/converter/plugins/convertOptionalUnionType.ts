import ts, {SyntaxKind} from "typescript";
import {ConverterPlugin} from "../plugin";

export const convertOptionalUnionType: ConverterPlugin = (node, context, render) => {
    if (
        ts.isUnionTypeNode(node) &&
        node.types.length === 2 &&
        node.types.some(type => (
            type.kind === SyntaxKind.NullKeyword ||
            type.kind === SyntaxKind.UndefinedKeyword
        ))
    ) {
        context.cover(node)
        context.cover(node.types[0])
        context.cover(node.types[1])

        const nonNullableType = node.types.find((type => (
            type.kind !== SyntaxKind.NullKeyword &&
            type.kind !== SyntaxKind.UndefinedKeyword
        )))

        return (nonNullableType && `${render(nonNullableType)}?`) ?? ""
    }

    return null;
}

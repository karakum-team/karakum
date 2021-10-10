import {ConverterPlugin} from "../plugin";
import ts, {SyntaxKind} from "typescript";

export const convertPrefixUnaryExpression: ConverterPlugin = (node, context) =>  {
    if (!ts.isPrefixUnaryExpression(node)) return null
    context.cover(node)

    if (node.operator === SyntaxKind.TildeToken) {
        return `inv(${node.operand})`
    }

    return `${node.operator}${node.operand}`
}

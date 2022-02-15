import ts, {SyntaxKind} from "typescript";
import {createSimplePlugin} from "../plugin";

export const convertReadonlyArrayType = createSimplePlugin((node, context, render) => {
    if (
        ts.isTypeOperatorNode(node) &&
        node.operator === SyntaxKind.ReadonlyKeyword &&
        ts.isArrayTypeNode(node.type)
    ) {
        context.cover(node)
        context.cover(node.type)

        return `Array<out ${render(node.type.elementType)}>`
    }



    return null;
})

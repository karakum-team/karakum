import ts from "typescript";
import {createSimplePlugin} from "../plugin";

export const convertArrayType = createSimplePlugin((node, context, render) => {
    if (!ts.isArrayTypeNode(node)) return null
    context.cover(node)

    return `Array<${render(node.elementType)}>`
})

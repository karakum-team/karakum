import ts from "typescript";
import {createSimplePlugin} from "../plugin";

export const convertQualifiedName = createSimplePlugin((node, context, render) => {
    if (!ts.isQualifiedName(node)) return null
    context.cover(node)

    return `${render(node.left)}.${render(node.right)}`
})

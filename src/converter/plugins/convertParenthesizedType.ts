import ts from "typescript";
import {createSimplePlugin} from "../plugin";

export const convertParenthesizedType = createSimplePlugin((node, context, render) => {
    if (!ts.isParenthesizedTypeNode(node)) return null
    context.cover(node)

    return `(${render(node.type)})`
})

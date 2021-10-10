import ts from "typescript";
import {ConverterPlugin} from "../plugin";

export const convertParenthesizedType: ConverterPlugin = (node, context, render) => {
    if (!ts.isParenthesizedTypeNode(node)) return null
    context.cover(node)

    return `(${render(node.type)})`
}

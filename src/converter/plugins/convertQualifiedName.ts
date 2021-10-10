import ts from "typescript";
import {ConverterPlugin} from "../plugin";

export const convertQualifiedName: ConverterPlugin = (node, context, render) => {
    if (!ts.isQualifiedName(node)) return null
    context.cover(node)

    return `${render(node.left)}.${render(node.right)}`
}

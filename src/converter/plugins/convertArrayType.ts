import ts from "typescript";
import {ConverterPlugin} from "../plugin";

export const convertArrayType: ConverterPlugin = (node, context, render) => {
    if (!ts.isArrayTypeNode(node)) return null
    context.cover(node)

    return `Array<${render(node.elementType)}>`
}

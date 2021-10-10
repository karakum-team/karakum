import ts from "typescript";
import {ConverterPlugin} from "../plugin";

export const convertLiteralType: ConverterPlugin = (node, context, render) => {
    if (!ts.isLiteralTypeNode(node)) return null
    context.cover(node)

    return render(node.literal)
}

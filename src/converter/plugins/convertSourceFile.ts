import ts from "typescript";
import {ConverterPlugin} from "../plugin";

export const convertSourceFile: ConverterPlugin = (node, context, render) => {
    if (!ts.isSourceFile(node)) return null
    context.cover(node)
    context.cover(node.endOfFileToken)

    return node.statements
        .map(statement => render(statement))
        .join("\n")
}

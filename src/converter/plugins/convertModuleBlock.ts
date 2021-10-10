import ts from "typescript";
import {ConverterPlugin} from "../plugin";

export const convertModuleBlock: ConverterPlugin = (node, context, render) => {
    if (!ts.isModuleBlock(node)) return null
    context.cover(node)

    return node.statements
        .map(statement => render(statement))
        .join("\n")
}

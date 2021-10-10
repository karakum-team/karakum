import ts from "typescript";
import {ConverterPlugin} from "../plugin";

export const convertModuleDeclaration: ConverterPlugin = (node, context, render) => {
    if (!ts.isModuleDeclaration(node)) return null
    context.cover(node)

    return (node.body && render(node.body)) ?? ""
}

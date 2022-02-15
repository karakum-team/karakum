import ts from "typescript";
import {createSimplePlugin} from "../plugin";

export const convertModuleDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isModuleDeclaration(node)) return null
    context.cover(node)

    return (node.body && render(node.body)) ?? ""
})

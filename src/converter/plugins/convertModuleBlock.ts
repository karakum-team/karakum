import ts from "typescript";
import {createSimplePlugin} from "../plugin";

export const convertModuleBlock = createSimplePlugin((node, context, render) => {
    if (!ts.isModuleBlock(node)) return null
    context.cover(node)

    return node.statements
        .map(statement => render(statement))
        .join("\n")
})

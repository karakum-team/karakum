import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";

export const convertModuleBlock = createSimplePlugin((node, context, render) => {
    if (!ts.isModuleBlock(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    return node.statements
        .map(statement => render(statement))
        .join("\n")
})

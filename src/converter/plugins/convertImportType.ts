import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";

export const convertImportType = createSimplePlugin((node, context, render) => {
    if (!ts.isImportTypeNode(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    checkCoverageService?.cover(node)
    checkCoverageService?.deepCover(node.argument)

    const qualifier = node.qualifier
        ? render(node.qualifier)
        : "Any"

    return `/* import(${typeScriptService?.printNode(node.argument)}) */ ${qualifier}`
})

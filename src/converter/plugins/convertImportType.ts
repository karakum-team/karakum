import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin";

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

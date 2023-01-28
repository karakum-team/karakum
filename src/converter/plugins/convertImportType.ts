import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertImportType = createSimplePlugin((node, context, render) => {
    if (!ts.isImportTypeNode(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)
    checkCoverageService?.deepCover(node.argument)

    const qualifier = node.qualifier
        ? render(node.qualifier)
        : "Any?"

    return `/* import(${node.argument.getText()}) */ ${qualifier}`
})

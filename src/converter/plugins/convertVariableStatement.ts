import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";

export const convertVariableStatement = createSimplePlugin((node, context, render) => {
    if (!ts.isVariableStatement(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)
    checkCoverageService?.cover(node.declarationList)

    const exportModifier = node.modifiers?.find(it => it.kind === ts.SyntaxKind.ExportKeyword)
    exportModifier && checkCoverageService?.cover(exportModifier)

    return node.declarationList.declarations
        .map(declaration => render(declaration))
        .join("\n")
})

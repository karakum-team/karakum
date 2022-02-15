import ts, {SyntaxKind} from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertVariableStatement = createSimplePlugin((node, context, render) => {
    if (!ts.isVariableStatement(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)
    checkCoverageService?.cover(node.declarationList)

    const exportModifier = node.modifiers?.find(it => it.kind === SyntaxKind.ExportKeyword)
    exportModifier && checkCoverageService?.cover(exportModifier)

    return node.declarationList.declarations
        .map(declaration => render(declaration))
        .join("\n")
})

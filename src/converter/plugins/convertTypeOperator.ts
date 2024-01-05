import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";

export const convertTypeOperator = createSimplePlugin((node, context) => {
    if (!ts.isTypeOperatorNode(node)) return null

    if (
        node.operator === ts.SyntaxKind.UniqueKeyword
        && node.type.kind === ts.SyntaxKind.SymbolKeyword
    ) {
        const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
        checkCoverageService?.cover(node)
        checkCoverageService?.cover(node.type)

        return "/* unique */ js.symbol.Symbol"
    }

    return null
})

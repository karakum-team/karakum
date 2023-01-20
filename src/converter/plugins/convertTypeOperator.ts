import ts, {SyntaxKind} from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertTypeOperator = createSimplePlugin((node, context) => {
    if (!ts.isTypeOperatorNode(node)) return null

    if (
        node.operator === SyntaxKind.UniqueKeyword
        && node.type.kind === SyntaxKind.SymbolKeyword
    ) {
        const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
        checkCoverageService?.cover(node)
        checkCoverageService?.cover(node.type)

        return "/* unique */ js.core.Symbol"
    }

    return null
})

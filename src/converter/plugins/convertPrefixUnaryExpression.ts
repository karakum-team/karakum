import {createSimplePlugin} from "../plugin";
import ts, {SyntaxKind} from "typescript";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertPrefixUnaryExpression = createSimplePlugin((node, context) =>  {
    if (!ts.isPrefixUnaryExpression(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    if (node.operator === SyntaxKind.TildeToken) {
        return `inv(${node.operand})`
    }

    return `${node.operator}${node.operand}`
})

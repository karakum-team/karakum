import {createSimplePlugin} from "../plugin.js";
import ts from "typescript";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";

export const convertPrefixUnaryExpression = createSimplePlugin((node, context) =>  {
    if (!ts.isPrefixUnaryExpression(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    if (node.operator === ts.SyntaxKind.TildeToken) {
        return `inv(${node.operand})`
    }

    return `${node.operator}${node.operand}`
})

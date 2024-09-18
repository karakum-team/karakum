import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";

export const convertArrayType = createSimplePlugin((node, context, render) => {
    if (
        ts.isTypeOperatorNode(node) &&
        node.operator === ts.SyntaxKind.ReadonlyKeyword &&
        ts.isArrayTypeNode(node.type)
    ) {
        const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
        checkCoverageService?.cover(node)
        checkCoverageService?.cover(node.type)

        return `js.array.ReadonlyArray<${render(node.type.elementType)}>`
    }

    if (ts.isArrayTypeNode(node)) {
        const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
        checkCoverageService?.cover(node)

        return `js.array.ReadonlyArray<${render(node.elementType)}>`
    }

    return null
})

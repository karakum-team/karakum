import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertArrayType = createSimplePlugin((node, context, render) => {
    if (!ts.isArrayTypeNode(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    return `Array<${render(node.elementType)}>`
})

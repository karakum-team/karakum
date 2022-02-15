import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertQualifiedName = createSimplePlugin((node, context, render) => {
    if (!ts.isQualifiedName(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    return `${render(node.left)}.${render(node.right)}`
})

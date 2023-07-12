import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";

export const convertPropertyAccessExpression = createSimplePlugin((node, context, render) => {
    if (!ts.isPropertyAccessExpression(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    return `${render(node.expression)}.${render(node.name)}`
})

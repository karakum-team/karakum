import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {convertParameterDeclarations} from "./convertParameterDeclaration.js";

export const convertConstructorDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isConstructorDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    return convertParameterDeclarations(node, context, render, {
        strategy: "function",
        template: parameters => {
            return `constructor (${parameters})`
        }
    })
})

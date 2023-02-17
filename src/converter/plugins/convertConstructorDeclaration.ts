import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {convertParameterDeclarations} from "./convertParameterDeclaration";

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

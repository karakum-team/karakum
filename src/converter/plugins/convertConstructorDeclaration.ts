import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertConstructorDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isConstructorDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const parameters = node.parameters
        ?.map(parameter => render(parameter))
        ?.join(", ")

    return `constructor (${parameters})`
})

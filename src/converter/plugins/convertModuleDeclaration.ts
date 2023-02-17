import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertModuleDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isModuleDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const name = render(node.name)

    const body = (node.body && render(node.body)) ?? ""

    return `
external object ${name} {
${body}
}
    `
})

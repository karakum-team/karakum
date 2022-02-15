import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertEnumDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isEnumDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const name = render(node.name)

    const members = node.members
        .map(member => render(member))
        .join(",\n")

    return `
external enum class ${name} {
${members}
}
    `
})

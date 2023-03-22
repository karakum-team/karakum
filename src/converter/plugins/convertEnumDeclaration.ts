import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertEnumDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isEnumDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const name = render(node.name)

    const members = node.members
        .map(member => `${render(member)}: ${name}`)
        .join("\n")

    return `
@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
sealed external interface ${name} {
companion object {
${members}
}
}
    `
})

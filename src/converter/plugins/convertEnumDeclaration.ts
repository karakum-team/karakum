import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {DeclarationMergingService, declarationMergingServiceKey} from "./DeclarationMergingPlugin.js";

export const convertEnumDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isEnumDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const declarationMergingService = context.lookupService<DeclarationMergingService>(declarationMergingServiceKey)
    if (declarationMergingService?.isCovered(node)) return ""
    declarationMergingService?.cover(node)

    const name = render(node.name)

    const members = (declarationMergingService?.getMembers(node) ?? node.members)
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

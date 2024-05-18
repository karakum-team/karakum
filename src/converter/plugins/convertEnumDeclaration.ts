import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {DeclarationMergingService, declarationMergingServiceKey} from "./DeclarationMergingPlugin.js";
import {NamespaceInfoService, namespaceInfoServiceKey} from "./NamespaceInfoPlugin.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";

export const convertEnumDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isEnumDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const declarationMergingService = context.lookupService<DeclarationMergingService>(declarationMergingServiceKey)
    if (declarationMergingService?.isCovered(node)) return ""
    declarationMergingService?.cover(node)

    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    const namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)

    const name = render(node.name)

    const namespace = typeScriptService?.findClosest(node, ts.isModuleDeclaration)

    let externalModifier = "external "

    if (namespace !== undefined && namespaceInfoService?.resolveNamespaceStrategy(namespace) === "object") {
        externalModifier = ""
    }

    const resolveNamespaceStrategy = namespaceInfoService?.resolveNamespaceStrategy?.bind(namespaceInfoService)

    const members = (declarationMergingService?.getMembers(node, resolveNamespaceStrategy) ?? node.members)
        .map(member => `${render(member)}: ${name}`)
        .join("\n")

    return `
sealed ${externalModifier}interface ${name} {
companion object {
${members}
}
}
    `
})

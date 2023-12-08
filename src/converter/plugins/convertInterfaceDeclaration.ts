import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {ifPresent} from "../render.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {InheritanceModifierService, inheritanceModifierServiceKey} from "./InheritanceModifierPlugin.js";
import {DeclarationMergingService, declarationMergingServiceKey} from "./DeclarationMergingPlugin.js";
import {NamespaceInfoService, namespaceInfoServiceKey} from "./NamespaceInfoPlugin.js";
import {InjectionService, injectionServiceKey} from "./InjectionPlugin.js";

export const convertInterfaceDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isInterfaceDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const declarationMergingService = context.lookupService<DeclarationMergingService>(declarationMergingServiceKey)
    if (declarationMergingService?.isCovered(node)) return ""
    declarationMergingService?.cover(node)

    const namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)
    const inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)
    const injectionService = context.lookupService<InjectionService>(injectionServiceKey)

    const exportModifier = node.modifiers?.find(it => it.kind === ts.SyntaxKind.ExportKeyword)
    exportModifier && checkCoverageService?.cover(exportModifier)

    const name = render(node.name)

    const inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)
    const injections = injectionService?.resolveInjections(node, context, render)

    const typeParameters = node.typeParameters
        ?.map(typeParameter => render(typeParameter))
        ?.join(", ")

    const heritageClauses = node.heritageClauses
        ?.map(heritageClause => render(heritageClause))
        ?.join(", ")

    const resolveNamespaceStrategy = namespaceInfoService?.resolveNamespaceStrategy?.bind(namespaceInfoService)

    const members = (declarationMergingService?.getMembers(node, resolveNamespaceStrategy) ?? node.members)
        .map(member => render(member))
        .join("\n")

    const injectedMembers = (injections ?? [])
        .join("\n")

    return `
${ifPresent(inheritanceModifier, it => `${it} `)}external interface ${name}${ifPresent(typeParameters, it => `<${it}>`)}${ifPresent(heritageClauses, it => ` : ${it}`)} {
${members}${ifPresent(injectedMembers, it => `\n${it}`)}
}
    `
})

import ts, {Declaration, ModifierLike, SyntaxKind} from "typescript";
import {createSimplePlugin} from "../plugin";
import {ifPresent} from "../render";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {InheritanceModifierService, inheritanceModifierServiceKey} from "./InheritanceModifierPlugin";
import {DeclarationMergingService, declarationMergingServiceKey} from "./DeclarationMergingPlugin";

function extractModifiers(member: Declaration): ModifierLike[] {
    if (
        ts.isPropertyDeclaration(member)
        || ts.isMethodDeclaration(member)
        || ts.isConstructorDeclaration(member)
        || ts.isGetAccessorDeclaration(member)
        || ts.isSetAccessorDeclaration(member)
        || ts.isIndexSignatureDeclaration(member)
    ) {
        return Array.from(member.modifiers ?? [])
    }

    return []
}

function filterPublicMembers(members: Declaration[]): readonly Declaration[] {
    return members.filter(member => extractModifiers(member).every(it => it.kind !== SyntaxKind.PrivateKeyword))
}

export const convertClassDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isClassDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const declarationMergingService = context.lookupService<DeclarationMergingService>(declarationMergingServiceKey)

    const inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)

    const exportModifier = node.modifiers?.find(it => it.kind === SyntaxKind.ExportKeyword)
    exportModifier && checkCoverageService?.cover(exportModifier)

    const declareModifier = node.modifiers?.find(it => it.kind === SyntaxKind.DeclareKeyword)
    declareModifier && checkCoverageService?.cover(declareModifier)

    const name = (node.name && render(node.name)) ?? "Anonymous"

    const inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)

    const typeParameters = node.typeParameters
        ?.map(typeParameter => render(typeParameter))
        ?.join(", ")

    const heritageClauses = node.heritageClauses
        ?.map(heritageClause => render(heritageClause))
        ?.join(", ")

    const mergedMembers = declarationMergingService?.getMembers(node) ?? Array.from(node.members)

    // cover private members
    mergedMembers
        .filter(member => extractModifiers(member).some(it => it.kind === SyntaxKind.PrivateKeyword))
        .forEach(member => checkCoverageService?.cover(member))

    const members = filterPublicMembers(mergedMembers)
        .map(member => render(member))
        .join("\n")

    // do not use merging for static members
    const staticMembers = filterPublicMembers(Array.from(node.members))
        .filter(member => extractModifiers(member).some(it => it.kind === SyntaxKind.StaticKeyword))
        .map(member => render(member))
        .join("\n")

    let companionObject = ""

    if (staticMembers.length > 0) {
        companionObject = `
        
companion object {
${staticMembers}
}
        `
    }

    return `
${ifPresent(inheritanceModifier, it => `${it} `)}external class ${name}${ifPresent(typeParameters, it => `<${it}>`)}${ifPresent(heritageClauses, it => ` : ${it}`)} {
${members}${companionObject}
}
    `
})

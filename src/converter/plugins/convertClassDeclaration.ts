import ts, {
    ClassElement,
    ConstructorDeclaration,
    GetAccessorDeclaration, IndexSignatureDeclaration,
    MethodDeclaration,
    PropertyDeclaration, SetAccessorDeclaration,
    SyntaxKind
} from "typescript";
import {createSimplePlugin} from "../plugin";
import {ifPresent} from "../render";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {InheritanceModifierService, inheritanceModifierServiceKey} from "./InheritanceModifierPlugin";

type SupportedClassElement =
    | PropertyDeclaration
    | MethodDeclaration
    | ConstructorDeclaration
    | GetAccessorDeclaration
    | SetAccessorDeclaration
    | IndexSignatureDeclaration

function filterSupportedMembers(members: readonly ClassElement[]): readonly SupportedClassElement[] {
    return members.filter((member): member is SupportedClassElement => {
        return (
            ts.isPropertyDeclaration(member)
            || ts.isMethodDeclaration(member)
            || ts.isConstructorDeclaration(member)
            || ts.isGetAccessorDeclaration(member)
            || ts.isSetAccessorDeclaration(member)
            || ts.isIndexSignatureDeclaration(member)
        )
    })
}

export const convertClassDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isClassDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

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

    const supportedMembers = filterSupportedMembers(node.members)

    const members = supportedMembers
        .filter(member => (member.modifiers ?? []).every(it => it.kind !== SyntaxKind.StaticKeyword))
        .map(member => render(member))
        .join("\n")

    const staticMembers = supportedMembers
        .filter(member => (member.modifiers ?? []).some(it => it.kind === SyntaxKind.StaticKeyword))
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

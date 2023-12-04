import ts, {ClassDeclaration, Declaration, ModifierLike} from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {ifPresent} from "../render.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {InheritanceModifierService, inheritanceModifierServiceKey} from "./InheritanceModifierPlugin.js";
import {DeclarationMergingService, declarationMergingServiceKey} from "./DeclarationMergingPlugin.js";
import {ConverterContext} from "../context.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";
import {NamespaceInfoService, namespaceInfoServiceKey} from "./NamespaceInfoPlugin.js";
import {InjectionService, injectionServiceKey} from "./InjectionPlugin.js";

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

function resolveConstructors(node: ClassDeclaration, members: Declaration[], context: ConverterContext): Declaration[] {
    const constructors = members.filter(it => ts.isConstructorDeclaration(it))
    if (constructors.length > 0) return constructors

    if (!node.heritageClauses) return constructors

    const heritageClause = node.heritageClauses.find(it => it.token === ts.SyntaxKind.ExtendsKeyword)
    if (!heritageClause) return constructors

    const parentReference = heritageClause.types[0].expression

    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    const typeChecker = typeScriptService?.program.getTypeChecker()
    const parentSymbol = typeChecker?.getSymbolAtLocation(parentReference)
    if (!parentSymbol) return constructors

    const parentDeclaration = parentSymbol.valueDeclaration
    if (!parentDeclaration || !ts.isClassDeclaration(parentDeclaration)) return constructors

    const declarationMergingService = context.lookupService<DeclarationMergingService>(declarationMergingServiceKey)
    const namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)

    const resolveNamespaceStrategy = namespaceInfoService?.resolveNamespaceStrategy?.bind(namespaceInfoService)

    const mergedMembers = declarationMergingService?.getMembers(parentDeclaration, resolveNamespaceStrategy) ?? Array.from(parentDeclaration.members)

    return resolveConstructors(parentDeclaration, mergedMembers, context)
}

export const convertClassDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isClassDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const declarationMergingService = context.lookupService<DeclarationMergingService>(declarationMergingServiceKey)
    declarationMergingService?.cover(node)

    const namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)
    const inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)
    const injectionService = context.lookupService<InjectionService>(injectionServiceKey)

    const exportModifier = node.modifiers?.find(it => it.kind === ts.SyntaxKind.ExportKeyword)
    exportModifier && checkCoverageService?.cover(exportModifier)

    const declareModifier = node.modifiers?.find(it => it.kind === ts.SyntaxKind.DeclareKeyword)
    declareModifier && checkCoverageService?.cover(declareModifier)

    const name = (node.name && render(node.name)) ?? "Anonymous"

    const inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)
    const injections = injectionService?.resolveInjections(node, context)

    const typeParameters = node.typeParameters
        ?.map(typeParameter => render(typeParameter))
        ?.join(", ")

    const heritageClauses = node.heritageClauses
        ?.map(heritageClause => render(heritageClause))
        ?.join(", ")

    const resolveNamespaceStrategy = namespaceInfoService?.resolveNamespaceStrategy?.bind(namespaceInfoService)

    const mergedMembers = declarationMergingService?.getMembers(node, resolveNamespaceStrategy) ?? Array.from(node.members)

    // cover private members
    mergedMembers
        .filter(member => extractModifiers(member).some(it => it.kind === ts.SyntaxKind.PrivateKeyword))
        .forEach(member => checkCoverageService?.cover(member))

    const publicMembers = mergedMembers
        .filter(member => extractModifiers(member).every(it => it.kind !== ts.SyntaxKind.PrivateKeyword))

    const constructors = resolveConstructors(node, publicMembers, context)
    const otherMembers = publicMembers.filter(it => !ts.isConstructorDeclaration(it))

    const members = [...constructors, ...otherMembers]
        .filter(member => extractModifiers(member).every(it => it.kind !== ts.SyntaxKind.StaticKeyword))
        .map(member => render(member))
        .join("\n")

    const staticMembers = otherMembers
        .filter(member => extractModifiers(member).some(it => it.kind === ts.SyntaxKind.StaticKeyword))
        .map(member => render(member))
        .join("\n")

    const injectedMembers = (injections ?? [])
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
${members}${ifPresent(injectedMembers, it => `\n${it}`)}${companionObject}
}
    `
})

import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {ifPresent} from "../render.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {InheritanceModifierService, inheritanceModifierServiceKey} from "./InheritanceModifierPlugin.js";
import {convertStringUnionType, isStringUnionType} from "./StringUnionTypePlugin.js";
import {convertTypeLiteral} from "./TypeLiteralPlugin.js";
import {convertInheritedTypeLiteral, isInheritedTypeLiteral} from "./InheritedTypeLiteralPlugin.js";
import {InjectionService, injectionServiceKey} from "./InjectionPlugin.js";

export const convertTypeAliasDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isTypeAliasDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)
    const injectionService = context.lookupService<InjectionService>(injectionServiceKey)

    const exportModifier = node.modifiers?.find(it => it.kind === ts.SyntaxKind.ExportKeyword)
    exportModifier && checkCoverageService?.cover(exportModifier)

    const declareModifier = node.modifiers?.find(it => it.kind === ts.SyntaxKind.DeclareKeyword)
    declareModifier && checkCoverageService?.cover(declareModifier)

    const name = render(node.name)

    const inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)
    const injections = injectionService?.resolveInjections(node, context)

    const typeParameters = node.typeParameters
        ?.map(typeParameter => render(typeParameter))
        ?.join(", ")

    if (ts.isTypeLiteralNode(node.type)) {
        return convertTypeLiteral(node.type, name, typeParameters, context, render)
    }

    if (isStringUnionType(node.type, context)) {
        return convertStringUnionType(node.type, name, context).declaration
    }

    if (isInheritedTypeLiteral(node.type)) {
        return convertInheritedTypeLiteral(node.type, name, typeParameters, context, render)
    }

    if (ts.isMappedTypeNode(node.type)) {
        const accessors = render(node.type)

        const injectedMembers = (injections ?? [])
            .join("\n")

        return `
${ifPresent(inheritanceModifier, it => `${it} `)}external interface ${name}${ifPresent(typeParameters, it => `<${it}> `)} {
${accessors}${ifPresent(injectedMembers, it => `\n${it}`)}
}
        `
    }

    if (
        ts.isFunctionTypeNode(node.type)
        && node.type.typeParameters
    ) {
        checkCoverageService?.cover(node.type)

        const mergedTypeParameters = [
            ...node.typeParameters ?? [],
            ...node.type.typeParameters,
        ]
            .map(typeParameter => render(typeParameter))
            .join(", ")

        const parameters = node.type.parameters
            ?.map(parameter => render(parameter))
            ?.join(", ")

        const returnType = render(node.type.type)

        const type = `(${parameters}) -> ${returnType}`

        return `typealias ${name}${ifPresent(mergedTypeParameters, it => `<${it}>`)} = ${type}`
    }

    const type = render(node.type)

    return `typealias ${name}${ifPresent(typeParameters, it => `<${it}>`)} = ${type}`
})

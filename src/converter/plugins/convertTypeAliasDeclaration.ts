import ts, {MappedTypeNode, TypeLiteralNode, TypeReferenceNode} from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {ifPresent} from "../render.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {InheritanceModifierService, inheritanceModifierServiceKey} from "./InheritanceModifierPlugin.js";
import {convertStringUnionType, isStringUnionType} from "./StringUnionTypePlugin.js";

export const convertTypeAliasDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isTypeAliasDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)

    const exportModifier = node.modifiers?.find(it => it.kind === ts.SyntaxKind.ExportKeyword)
    exportModifier && checkCoverageService?.cover(exportModifier)

    const declareModifier = node.modifiers?.find(it => it.kind === ts.SyntaxKind.DeclareKeyword)
    declareModifier && checkCoverageService?.cover(declareModifier)

    const name = render(node.name)

    const inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)

    const typeParameters = node.typeParameters
        ?.map(typeParameter => render(typeParameter))
        ?.join(", ")

    if (ts.isTypeLiteralNode(node.type)) {
        checkCoverageService?.cover(node.type)

        const members = node.type.members
            .map(member => render(member))
            .join("\n")

        return `
${ifPresent(inheritanceModifier, it => `${it} `)}external interface ${name}${ifPresent(typeParameters, it => `<${it}> `)} {
${members}
}
        `
    }

    if (ts.isMappedTypeNode(node.type)) {
        const accessors = render(node.type)

        return `
${ifPresent(inheritanceModifier, it => `${it} `)}external interface ${name}${ifPresent(typeParameters, it => `<${it}> `)} {
${accessors}
}
        `
    }

    if (isStringUnionType(node.type)) {
        return convertStringUnionType(node.type, name, context)
    }

    if (
        ts.isIntersectionTypeNode(node.type)
        && node.type.types.every(it => (
            ts.isTypeReferenceNode(it)
            || ts.isTypeLiteralNode(it)
            || ts.isMappedTypeNode(it)
        ))
    ) {
        checkCoverageService?.cover(node.type)
        node.type.types.forEach(it => checkCoverageService?.cover(it))

        const typeReferences = node.type.types.filter((it): it is TypeReferenceNode => ts.isTypeReferenceNode(it))
        const typeLiterals = node.type.types.filter((it): it is TypeLiteralNode => ts.isTypeLiteralNode(it))
        const mappedType = node.type.types.find((it): it is MappedTypeNode => ts.isMappedTypeNode(it))

        const heritageTypes = typeReferences
            .map(type => render(type))
            .join(", ")

        const heritageClause = ifPresent(heritageTypes, it => ` : ${it}`)

        const members = typeLiterals
            .flatMap(it => it.members)
            .map(member => render(member))
            .join("\n")

        let accessors = ""

        if (mappedType) {
            accessors = render(mappedType)
        }

        return `
${ifPresent(inheritanceModifier, it => `${it} `)}external interface ${name}${ifPresent(typeParameters, it => `<${it}> `)}${heritageClause} {
${ifPresent(accessors, it => `${it}\n`)}${members}
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

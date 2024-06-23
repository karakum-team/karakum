import ts, {IntersectionTypeNode, MappedTypeNode, TypeLiteralNode, TypeReferenceNode} from "typescript";
import {ifPresent, Render} from "../render.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {InheritanceModifierService, inheritanceModifierServiceKey} from "./InheritanceModifierPlugin.js";
import {createAnonymousDeclarationPlugin} from "./AnonymousDeclarationPlugin.js";
import {extractTypeParameters, renderDeclaration, renderReference} from "../extractTypeParameters.js";
import {ConverterContext} from "../context.js";
import {InjectionService, injectionServiceKey} from "./InjectionPlugin.js";
import {convertMappedTypeBody} from "./MappedTypePlugin.js";
import {convertTypeLiteralBody} from "./TypeLiteralPlugin.js";
import {InjectionType} from "../injection.js";

export function isInheritedTypeLiteral(node: ts.Node): node is IntersectionTypeNode {
    return (
        ts.isIntersectionTypeNode(node)
        && node.types.every(it => (
            ts.isTypeReferenceNode(it)
            || ts.isTypeLiteralNode(it)
            || ts.isMappedTypeNode(it)
        ))
    )
}

export function convertInheritedTypeLiteral(
    node: IntersectionTypeNode,
    name: string,
    typeParameters: string | undefined,
    context: ConverterContext,
    render: Render,
) {
    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)
    const injectionService = context.lookupService<InjectionService>(injectionServiceKey)

    const inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)
    const injections = injectionService?.resolveInjections(node, InjectionType.MEMBER, context, render)
    const heritageInjections = injectionService?.resolveInjections(node, InjectionType.HERITAGE_CLAUSE, context, render)

    const typeReferences = node.types.filter((it): it is TypeReferenceNode => ts.isTypeReferenceNode(it))
    const typeLiterals = node.types.filter((it): it is TypeLiteralNode => ts.isTypeLiteralNode(it))
    const mappedType = node.types.find((it): it is MappedTypeNode => ts.isMappedTypeNode(it))

    const heritageTypes = typeReferences
        .map(type => render(type))
        .filter(Boolean)
        .join(", ")

    const injectedHeritageClauses = heritageInjections
        ?.filter(Boolean)
        ?.join(", ")

    const fullHeritageClauses = [heritageTypes, injectedHeritageClauses]
        .filter(Boolean)
        .join(", ")

    const members = typeLiterals
        .map(it => convertTypeLiteralBody(it, context, render))
        .join("\n")

    let accessors = ""

    if (mappedType) {
        accessors = convertMappedTypeBody(mappedType, context, render)
    }

    const injectedMembers = (injections ?? [])
        .join("\n")

    return `
${ifPresent(inheritanceModifier, it => `${it} `)}external interface ${name}${ifPresent(typeParameters, it => `<${it}> `)}${(ifPresent(fullHeritageClauses, it => ` : ${it}`))} {
${ifPresent(accessors, it => `${it}\n`)}${members}${ifPresent(injectedMembers, it => `\n${it}`)}
}
    `.trim()
}

export const inheritedTypeLiteralPlugin = createAnonymousDeclarationPlugin(
    (node, context, render) => {
        if (!isInheritedTypeLiteral(node)) return null

        const name = context.resolveName(node)

        const typeParameters = extractTypeParameters(node, context)

        const declaration = convertInheritedTypeLiteral(node, name, renderDeclaration(typeParameters, render), context, render)

        const reference = `${name}${ifPresent(renderReference(typeParameters, render), it => `<${it}>`)}`

        return {name, declaration, reference};
    }
)

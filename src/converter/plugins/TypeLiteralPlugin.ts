import ts, {TypeLiteralNode} from "typescript";
import {ifPresent, Render} from "../render.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {InheritanceModifierService, inheritanceModifierServiceKey} from "./InheritanceModifierPlugin.js";
import {createAnonymousDeclarationPlugin} from "./AnonymousDeclarationPlugin.js";
import {extractTypeParameters, renderDeclaration, renderReference} from "../extractTypeParameters.js";
import {ConverterContext} from "../context.js";
import {InjectionService, injectionServiceKey} from "./InjectionPlugin.js";
import {InjectionType} from "../injection.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";
import {NamespaceInfoService, namespaceInfoServiceKey} from "./NamespaceInfoPlugin.js";

export function convertTypeLiteralBody(node: TypeLiteralNode, context: ConverterContext, render: Render) {
    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const injectionService = context.lookupService<InjectionService>(injectionServiceKey)
    const injections = injectionService?.resolveInjections(node, InjectionType.MEMBER, context, render)

    const members = node.members
        .map(member => render(member))
        .join("\n")

    const injectedMembers = (injections ?? [])
        .join("\n")

    return `${members}${ifPresent(injectedMembers, it => `\n${it}`)}`
}

export function convertTypeLiteral(
    node: TypeLiteralNode,
    name: string,
    typeParameters: string | undefined,
    isInlined: boolean,
    context: ConverterContext,
    render: Render,
) {
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    const namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)
    const inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)
    const inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)

    const injectionService = context.lookupService<InjectionService>(injectionServiceKey)
    const heritageInjections = injectionService?.resolveInjections(node, InjectionType.HERITAGE_CLAUSE, context, render)

    const namespace = typeScriptService?.findClosest(node, ts.isModuleDeclaration)

    let externalModifier = "external "

    if (isInlined && namespace !== undefined && namespaceInfoService?.resolveNamespaceStrategy(namespace) === "object") {
        externalModifier = ""
    }

    const injectedHeritageClauses = heritageInjections
        ?.filter(Boolean)
        ?.join(", ")

    return `
${ifPresent(inheritanceModifier, it => `${it} `)}${externalModifier}interface ${name}${ifPresent(typeParameters, it => `<${it}>`)}${(ifPresent(injectedHeritageClauses, it => ` : ${it}`))} {
${convertTypeLiteralBody(node, context, render)}
}
    `.trim()
}

export const typeLiteralPlugin = createAnonymousDeclarationPlugin(
    (node, context, render) => {
        if (!ts.isTypeLiteralNode(node)) return null

        // handle empty type literal
        if (node.members.length === 0) return "Any"

        const name = context.resolveName(node)

        const typeParameters = extractTypeParameters(node, context)

        const declaration = convertTypeLiteral(node, name, renderDeclaration(typeParameters, render), false, context, render)

        const reference = `${name}${ifPresent(renderReference(typeParameters, render), it => `<${it}>`)}`

        return {name, declaration, reference};
    }
)

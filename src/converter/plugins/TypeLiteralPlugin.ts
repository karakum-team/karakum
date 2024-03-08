import ts, {TypeLiteralNode} from "typescript";
import {ifPresent, Render} from "../render.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {InheritanceModifierService, inheritanceModifierServiceKey} from "./InheritanceModifierPlugin.js";
import {createAnonymousDeclarationPlugin} from "./AnonymousDeclarationPlugin.js";
import {extractTypeParameters, renderDeclaration, renderReference} from "../extractTypeParameters.js";
import {ConverterContext} from "../context.js";
import {InjectionService, injectionServiceKey} from "./InjectionPlugin.js";

export function convertTypeLiteralBody(node: TypeLiteralNode, context: ConverterContext, render: Render) {
    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const injectionService = context.lookupService<InjectionService>(injectionServiceKey)
    const injections = injectionService?.resolveInjections(node, context, render)

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
    context: ConverterContext,
    render: Render,
) {
    const inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)
    const inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)

    return `
${ifPresent(inheritanceModifier, it => `${it} `)}external interface ${name}${ifPresent(typeParameters, it => `<${it}>`)} {
${convertTypeLiteralBody(node, context, render)}
}
    `
}

export const typeLiteralPlugin = createAnonymousDeclarationPlugin(
    (node, context, render) => {
        if (!ts.isTypeLiteralNode(node)) return null

        // handle empty type literal
        if (node.members.length === 0) return "Any"

        const name = context.resolveName(node)

        const typeParameters = extractTypeParameters(node, context)

        const declaration = convertTypeLiteral(node, name, renderDeclaration(typeParameters, render), context, render)

        const reference = `${name}${ifPresent(renderReference(typeParameters, render), it => `<${it}>`)}`

        return {name, declaration, reference};
    }
)

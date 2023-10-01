import ts, {TypeLiteralNode} from "typescript";
import {ifPresent, Render} from "../render.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {InheritanceModifierService, inheritanceModifierServiceKey} from "./InheritanceModifierPlugin.js";
import {createAnonymousDeclarationPlugin} from "./AnonymousDeclarationPlugin.js";
import {extractTypeParameters} from "../extractTypeParameters.js";
import {ConverterContext} from "../context.js";

export function convertTypeLiteral(
    node: TypeLiteralNode,
    name: string,
    typeParameters: string | undefined,
    context: ConverterContext,
    render: Render,
) {
    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    // handle empty type literal
    if (node.members.length === 0) return "Any"

    const inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)

    const inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)

    const members = node.members
        .map(member => render(member))
        .join("\n")

    return `
${ifPresent(inheritanceModifier, it => `${it} `)}external interface ${name}${ifPresent(typeParameters, it => `<${it}>`)} {
${members}
}
    `
}

export const typeLiteralPlugin = createAnonymousDeclarationPlugin(
    (node, context, render) => {
        if (!ts.isTypeLiteralNode(node)) return null

        const name = context.resolveName(node)

        const typeParameters = extractTypeParameters(node, context, render)

        const declaration = convertTypeLiteral(node, name, typeParameters.declaration, context, render)

        const reference = `${name}${ifPresent(typeParameters.reference, it => `<${it}>`)}`

        return {name, declaration, reference};
    }
)

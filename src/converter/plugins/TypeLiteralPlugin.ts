import ts from "typescript";
import {ifPresent} from "../render.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {InheritanceModifierService, inheritanceModifierServiceKey} from "./InheritanceModifierPlugin.js";
import {createAnonymousDeclarationPlugin} from "./AnonymousDeclarationPlugin.js";
import {extractTypeParameters} from "../../utils/extractTypeParameters.js";

export const typeLiteralPlugin = createAnonymousDeclarationPlugin(
    (node, context, render) => {
        if (!ts.isTypeLiteralNode(node)) return null

        const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
        checkCoverageService?.cover(node)

        // handle empty type literal
        if (node.members.length === 0) return "Any"

        const name = context.resolveName(node)

        const inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)

        const inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)

        const typeParameters = extractTypeParameters(node, context).join(", ")

        const members = node.members
            .map(member => render(member))
            .join("\n")

        const declaration = `
${ifPresent(inheritanceModifier, it => `${it} `)}external interface ${name}${ifPresent(typeParameters, it => `<${it}>`)} {
${members}
}
        `

        const reference = `${name}${ifPresent(typeParameters, it => `<${it}>`)}`

        return {name, declaration, reference};
    }
)

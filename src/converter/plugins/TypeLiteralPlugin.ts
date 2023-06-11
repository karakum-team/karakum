import ts from "typescript";
import {ifPresent} from "../render";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin";
import {InheritanceModifierService, inheritanceModifierServiceKey} from "./InheritanceModifierPlugin";
import {createAnonymousDeclarationPlugin} from "./AnonymousDeclarationPlugin";
import {extractTypeParameters} from "../../utils/extractTypeParameters";
import {defaultNameResolvers} from "../defaultNameResolvers";

export const createTypeLiteralPlugin = createAnonymousDeclarationPlugin(
    defaultNameResolvers,
    (node, context, render) => {
        if (!ts.isTypeLiteralNode(node)) return null

        const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
        checkCoverageService?.cover(node)

        // handle empty type literal
        if (node.members.length === 0) return "Any"

        const name = context.resolveName(node)

        const inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)

        const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

        const inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)

        const typeParameters = extractTypeParameters(node, typeScriptService?.program).join(", ")

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

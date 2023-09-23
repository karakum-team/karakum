import ts, {MappedTypeNode, TypeLiteralNode, TypeReferenceNode} from "typescript";
import {ifPresent} from "../render.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {InheritanceModifierService, inheritanceModifierServiceKey} from "./InheritanceModifierPlugin.js";
import {createAnonymousDeclarationPlugin} from "./AnonymousDeclarationPlugin.js";
import {extractTypeParameters} from "../../utils/extractTypeParameters.js";

export const inheritedTypeLiteralPlugin = createAnonymousDeclarationPlugin(
    (node, context, render) => {
        if (!ts.isIntersectionTypeNode(node)) return null
        if (!node.types.every(it => (
            ts.isTypeReferenceNode(it)
            || ts.isTypeLiteralNode(it)
            || ts.isMappedTypeNode(it)
        ))) return null

        const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
        checkCoverageService?.cover(node)

        const name = context.resolveName(node)

        const inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)

        const inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)

        const typeParameters = extractTypeParameters(node, context).join(", ")

        const typeReferences = node.types.filter((it): it is TypeReferenceNode => ts.isTypeReferenceNode(it))
        const typeLiterals = node.types.filter((it): it is TypeLiteralNode => ts.isTypeLiteralNode(it))
        const mappedType = node.types.find((it): it is MappedTypeNode => ts.isMappedTypeNode(it))

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

        const declaration = `
${ifPresent(inheritanceModifier, it => `${it} `)}external interface ${name}${ifPresent(typeParameters, it => `<${it}> `)}${heritageClause} {
${ifPresent(accessors, it => `${it}\n`)}${members}
}
        `

        const reference = `${name}${ifPresent(typeParameters, it => `<${it}>`)}`

        return {name, declaration, reference};
    }
)

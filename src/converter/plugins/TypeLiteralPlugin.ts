import ts, {TypeLiteralNode} from "typescript";
import {ifPresent} from "../render";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin";
import {NameResolver} from "../nameResolver";
import {resolveFunctionTypeAliasParameterName} from "../nameResolvers/resolveFunctionTypeAliasParameterName";
import {resolveFunctionParameterName} from "../nameResolvers/resolveFunctionParameterName";
import {resolveTypeAliasPropertyName} from "../nameResolvers/resolveTypeAliasPropertyName";
import {resolveCallSignatureParameterName} from "../nameResolvers/resolveCallSignatureParameterName";
import {resolveConstructorParameterName} from "../nameResolvers/resolveConstructorParameterName";
import {resolveClassMethodParameterName} from "../nameResolvers/resolveClassMethodParameterName";
import {resolveInterfaceMethodParameterName} from "../nameResolvers/resolveInterfaceMethodParameterName";
import {resolveFunctionReturnTypeName} from "../nameResolvers/resolveFunctionReturnTypeName";
import {resolveInterfaceMethodReturnTypeName} from "../nameResolvers/resolveInterfaceMethodReturnTypeName";
import {resolveClassMethodReturnTypeName} from "../nameResolvers/resolveClassMethodReturnTypeName";
import {InheritanceModifierService, inheritanceModifierServiceKey} from "./InheritanceModifierPlugin";
import {createAnonymousDeclarationPlugin} from "./AnonymousDeclarationPlugin";
import {extractTypeParameters} from "../../utils/extractTypeParameters";

const defaultNameResolvers: NameResolver<TypeLiteralNode>[] = [
    resolveFunctionParameterName,
    resolveFunctionReturnTypeName,
    resolveFunctionTypeAliasParameterName,
    resolveCallSignatureParameterName,
    resolveConstructorParameterName,
    resolveClassMethodParameterName,
    resolveClassMethodReturnTypeName,
    resolveInterfaceMethodParameterName,
    resolveInterfaceMethodReturnTypeName,
    resolveTypeAliasPropertyName,
]

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

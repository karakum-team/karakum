import ts, {MappedTypeNode} from "typescript";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {ifPresent, Render, renderNullable} from "../render.js";
import {InheritanceModifierService, inheritanceModifierServiceKey} from "./InheritanceModifierPlugin.js";
import {createAnonymousDeclarationPlugin} from "./AnonymousDeclarationPlugin.js";
import {extractTypeParameters, renderDeclaration, renderReference} from "../extractTypeParameters.js";
import {ConverterContext} from "../context.js";
import {InjectionService, injectionServiceKey} from "./InjectionPlugin.js";
import {InjectionType} from "../injection.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";
import {NamespaceInfoService, namespaceInfoServiceKey} from "./NamespaceInfoPlugin.js";

export function convertMappedTypeBody(node: MappedTypeNode, context: ConverterContext, render: Render) {
    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)
    const getterInheritanceModifier = inheritanceModifierService?.resolveGetterInheritanceModifier(node, context)
    const setterInheritanceModifier = inheritanceModifierService?.resolveSetterInheritanceModifier(node, context)

    const injectionService = context.lookupService<InjectionService>(injectionServiceKey)
    const injections = injectionService?.resolveInjections(node, InjectionType.MEMBER, context, render)

    const readonly = node.readonlyToken && node.readonlyToken.kind !== ts.SyntaxKind.MinusToken

    const typeParameter = render(node.typeParameter)

    const type = renderNullable(node.type, true, context, render)

    let keyType: string

    if (node.nameType) {
        keyType = render(node.nameType)
    } else {
        keyType = node.typeParameter.name.text
    }

    const getter = `
@seskar.js.JsNative
${ifPresent(getterInheritanceModifier, it => `${it} `)}operator fun <${typeParameter}> get(key: ${keyType}): ${type}
    `.trim()

    let setter = ""

    if (!readonly) {
        setter = `
@seskar.js.JsNative
${ifPresent(setterInheritanceModifier, it => `${it} `)}operator fun <${typeParameter}> set(key: ${keyType}, value: ${type})
        `.trim()
    }

    const injectedMembers = (injections ?? [])
        .join("\n")

    return `${getter}${ifPresent(setter, it => `\n\n${it}`)}${ifPresent(injectedMembers, it => `\n${it}`)}`
}

export const convertMappedType = (
    node: MappedTypeNode,
    name: string,
    typeParameters: string | undefined,
    isInlined: boolean,
    context: ConverterContext,
    render: Render,
) => {
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
${convertMappedTypeBody(node, context, render)}
}
    `.trim()
}

export const mappedTypePlugin = createAnonymousDeclarationPlugin(
    (node, context, render) => {
        if (!ts.isMappedTypeNode(node)) return null

        const name = context.resolveName(node)

        const typeParameters = extractTypeParameters(node, context)

        const declaration = convertMappedType(node, name, renderDeclaration(typeParameters, render), false, context, render)

        const reference = `${name}${ifPresent(renderReference(typeParameters, render), it => `<${it}>`)}`

        return {name, declaration, reference};
    }
)

import {createSimplePlugin} from "../plugin.js";
import ts from "typescript";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {ifPresent, renderNullable} from "../render.js";
import {InheritanceModifierService, inheritanceModifierServiceKey} from "./InheritanceModifierPlugin.js";

export const convertMappedType = createSimplePlugin((node, context, render) => {
    if (!ts.isMappedTypeNode(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)

    checkCoverageService?.cover(node)

    const inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)

    const inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)

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
${ifPresent(inheritanceModifier, it => `${it} `)}operator fun <${typeParameter}> get(key: ${keyType}): ${type}
    `

    let setter = ""

    if (!readonly) {
        setter = `
@seskar.js.JsNative
${ifPresent(inheritanceModifier, it => `${it} `)}operator fun <${typeParameter}> set(key: ${keyType}, value: ${type})
        `
    }


    return `
${getter}${ifPresent(setter, it => `\n\n${it}`)}
    `
})

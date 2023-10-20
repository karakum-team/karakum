import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {ifPresent, renderNullable} from "../render.js";
import {InheritanceModifierService, inheritanceModifierServiceKey} from "./InheritanceModifierPlugin.js";

export const convertIndexedSignatureDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isIndexSignatureDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)

    checkCoverageService?.cover(node)

    const inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)

    const inheritanceModifier = inheritanceModifierService?.resolveInheritanceModifier(node, context)

    const readonly = node.modifiers?.find(modifier => modifier.kind === ts.SyntaxKind.ReadonlyKeyword)
    readonly && checkCoverageService?.cover(readonly)

    let keyType: string

    if (node.parameters[0].type) {
        keyType = render(node.parameters[0].type)
    } else {
        keyType = "Any? /* type isn't declared */"
    }

    const type = renderNullable(node.type, true, context, render)

    const getter = `
@Suppress(
    "DEPRECATION",
    "NATIVE_INDEXER_KEY_SHOULD_BE_STRING_OR_NUMBER",
)
@nativeGetter
${ifPresent(inheritanceModifier, it => `${it} `)}operator fun get(key: ${keyType}): ${type}
    `

    let setter = ""

    if (!readonly) {
        setter = `
@Suppress(
    "DEPRECATION",
    "NATIVE_INDEXER_KEY_SHOULD_BE_STRING_OR_NUMBER",
)
@nativeSetter
${ifPresent(inheritanceModifier, it => `${it} `)}operator fun set(key: ${keyType}, value: ${type})
        `
    }


    return `
${getter}${ifPresent(setter, it => `\n\n${it}`)}
    `
})

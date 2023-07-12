import {createSimplePlugin} from "../plugin.js";
import ts from "typescript";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {ifPresent, renderNullable} from "../render.js";

export const convertMappedType = createSimplePlugin((node, context, render) => {
    if (!ts.isMappedTypeNode(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)

    checkCoverageService?.cover(node)

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
@Suppress(
    "DEPRECATION",
    "NATIVE_INDEXER_KEY_SHOULD_BE_STRING_OR_NUMBER",
)
@nativeGetter
operator fun <${typeParameter}> get(key: ${keyType}): ${type}
    `

    let setter = ""

    if (!readonly) {
        setter = `
@Suppress(
    "DEPRECATION",
    "NATIVE_INDEXER_KEY_SHOULD_BE_STRING_OR_NUMBER",
)
@nativeSetter
operator fun <${typeParameter}> set(key: ${keyType}, value: ${type})
        `
    }


    return `
${getter}${ifPresent(setter, it => `\n\n${it}`)}
    `
})

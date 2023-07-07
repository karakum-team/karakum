import ts, {SyntaxKind} from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {ifPresent, renderNullable} from "../render";

export const convertIndexedSignatureDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isIndexSignatureDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)

    checkCoverageService?.cover(node)

    const readonly = node.modifiers?.find(modifier => modifier.kind === SyntaxKind.ReadonlyKeyword)
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
operator fun get(key: ${keyType}): ${type}
    `

    let setter = ""

    if (!readonly) {
        setter = `
@Suppress(
    "DEPRECATION",
    "NATIVE_INDEXER_KEY_SHOULD_BE_STRING_OR_NUMBER",
)
@nativeSetter
operator fun set(key: ${keyType}, value: ${type})
        `
    }


    return `
${getter}${ifPresent(setter, it => `\n\n${it}`)}
    `
})

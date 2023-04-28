import ts, {SyntaxKind} from "typescript";
import {createSimplePlugin} from "../plugin";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {isPossiblyNullableType} from "./NullableUnionTypePlugin";
import {ifPresent} from "../render";

export const convertIndexedSignatureDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isIndexSignatureDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    checkCoverageService?.cover(node)

    const readonly = node.modifiers?.find(modifier => modifier.kind === SyntaxKind.ReadonlyKeyword)
    readonly && checkCoverageService?.cover(readonly)

    let keyType: string

    if (node.parameters[0].type) {
        keyType = render(node.parameters[0].type)
    } else {
        keyType = "Any? /* type isn't declared */"
    }

    let type: string

    if (node.type) {
        type = render(node.type)
    } else {
        type = "Any? /* type isn't declared */"
    }

    // handle `typeof` case
    const resolvedType = node.type && typeScriptService?.resolveType(node.type)
    let nullable = true
    if (
        !resolvedType
        || isPossiblyNullableType(resolvedType)
    ) {
        nullable = false
    }

    const getter = `
@Suppress(
    "DEPRECATION",
    "NATIVE_INDEXER_KEY_SHOULD_BE_STRING_OR_NUMBER",
)
@nativeGetter
operator fun get(key: ${keyType}): ${type}${nullable ? "?" : ""}
    `

    let setter = ""

    if (!readonly) {
        setter = `
@Suppress(
    "DEPRECATION",
    "NATIVE_INDEXER_KEY_SHOULD_BE_STRING_OR_NUMBER",
)
@nativeSetter
operator fun set(key: ${keyType}, value: ${type}${nullable ? "?" : ""})
        `
    }


    return `
${getter}${ifPresent(setter, it => `\n\n${it}`)}
    `
})

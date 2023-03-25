import {createSimplePlugin} from "../plugin";
import ts, {SyntaxKind} from "typescript";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin";
import {isPossiblyNullableType} from "./NullableUnionTypePlugin";
import {ifPresent} from "../render";

export const convertMappedType = createSimplePlugin((node, context, render) => {
    if (!ts.isMappedTypeNode(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    checkCoverageService?.cover(node)

    const readonly = node.readonlyToken && node.readonlyToken.kind !== SyntaxKind.MinusToken

    const typeParameter = render(node.typeParameter)

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
operator fun <${typeParameter}> get(key: ${keyType}): ${type}${nullable ? "?" : ""}
    `

    let setter = ""

    if (!readonly) {
        setter = `
@Suppress(
    "DEPRECATION",
    "NATIVE_INDEXER_KEY_SHOULD_BE_STRING_OR_NUMBER",
)
@nativeSetter
operator fun <${typeParameter}> set(key: ${keyType}, value: ${type}${nullable ? "?" : ""})
        `
    }


    return `
${getter}${ifPresent(setter, it => `\n\n${it}`)}
    `
})

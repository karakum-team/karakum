import ts, {SyntaxKind} from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {isPossiblyNullableType} from "./NullableUnionTypePlugin";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin";
import {escapeIdentifier} from "../../utils/strings";

export const convertPropertySignature = createSimplePlugin((node, context, render) => {
    if (!ts.isPropertySignature(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    checkCoverageService?.cover(node)

    const readonly = node.modifiers?.find(modifier => modifier.kind === SyntaxKind.ReadonlyKeyword)
    readonly && checkCoverageService?.cover(readonly)

    node.questionToken && checkCoverageService?.cover(node.questionToken)

    const modifier = readonly
        ? "val "
        : "var "

    const name = escapeIdentifier(render(node.name))

    let type: string

    if (node.type) {
        type = render(node.type)
    } else {
        type = "Any? /* type isn't declared */"
    }

    let isOptional = false

    // handle `typeof` case
    const resolvedType = node.type && typeScriptService?.resolveType(node.type)

    if (
        node.questionToken
        && resolvedType
        && !isPossiblyNullableType(resolvedType)
    ) {
        isOptional = true
    }

    return `${modifier}${name}: ${type}${isOptional ? "?" : ""}`
})

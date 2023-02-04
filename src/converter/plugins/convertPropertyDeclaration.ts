import ts, {SyntaxKind} from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {isNullableType, isNullableUnionType} from "./NullableUnionTypePlugin";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin";

export const convertPropertyDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isPropertyDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    checkCoverageService?.cover(node)

    const readonly = node.modifiers?.find(modifier => modifier.kind === SyntaxKind.ReadonlyKeyword)
    readonly && checkCoverageService?.cover(readonly)

    node.questionToken && checkCoverageService?.cover(node.questionToken)

    const modifier = readonly
        ? "val "
        : "var "

    const name = render(node.name)

    let type = node.type && render(node.type)

    if (!type) {
        type = "Any? /* some expression */"
    }

    let isOptional = false

    const resolvedType = node.type && typeScriptService?.resolveType(node.type)

    if (
        node.questionToken
        && resolvedType
        && resolvedType.kind !== SyntaxKind.UnknownKeyword
        && resolvedType.kind !== SyntaxKind.AnyKeyword
        && !isNullableType(resolvedType)
        && !(
            ts.isUnionTypeNode(resolvedType)
            && isNullableUnionType(resolvedType)
        )
    ) {
        isOptional = true
    }

    return `${modifier}${name}: ${type}${isOptional ? "?" : ""}`
})

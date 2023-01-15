import ts, {SyntaxKind} from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {isNullableUnionType} from "./NullableUnionTypePlugin";

export const convertPropertySignature = createSimplePlugin((node, context, render) => {
    if (!ts.isPropertySignature(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
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
        // throw new Error(`${name} property signature without type is unsupported`)
        type = "Any? /* some expression */" // TODO: resolve types
    }

    let isOptional = false

    if (
        node.questionToken
        && node.type?.kind !== SyntaxKind.UnknownKeyword
        && node.type?.kind !== SyntaxKind.AnyKeyword
        && !(
            node.type
            && ts.isUnionTypeNode(node.type)
            && isNullableUnionType(node.type)
        )
    ) {
        isOptional = true
    }

    return `${modifier}${name}: ${type}${isOptional ? "?" : ""}`
})

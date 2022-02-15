import ts, {SyntaxKind} from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertPropertySignature = createSimplePlugin((node, context, render) => {
    if (!ts.isPropertySignature(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const readonly = node.modifiers?.find(modifier => modifier.kind === SyntaxKind.ReadonlyKeyword)
    readonly && checkCoverageService?.cover(readonly)

    const modifier = readonly
        ? "val "
        : "var "

    const name = render(node.name)

    let type = node.type && render(node.type)

    if (!type) {
        // throw new Error(`${name} property signature without type is unsupported`)
        type = "Any? /* some expression */" // TODO: resolve types
    }

    return `${modifier}${name}: ${type}${node.questionToken ? "?" : ""}`
})

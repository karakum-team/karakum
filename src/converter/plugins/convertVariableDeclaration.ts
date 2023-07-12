import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";

export const convertVariableDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isVariableDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    // skip initializer
    node.initializer && checkCoverageService?.cover(node.initializer)

    const modifier = node.parent.flags & ts.NodeFlags.Const
        ? "val "
        : "var "

    const name = render(node.name)

    let type: string

    if (node.type) {
        type = render(node.type)
    } else {
        type = "Any? /* should be inferred */" // TODO: infer types
    }

    return `external ${modifier}${name}: ${type}`
})

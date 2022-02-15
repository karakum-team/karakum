import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertParameterDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isParameter(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)
    node.dotDotDotToken && checkCoverageService?.cover(node.dotDotDotToken)
    node.questionToken && checkCoverageService?.cover(node.questionToken)

    const name = render(node.name)

    let type = node.type && render(node.type)

    if (!type) {
        // throw new Error(`${name} parameter declaration without type is unsupported`)
        type = "Any? /* some expression */" // TODO: resolve types
    }

    if (node.dotDotDotToken) {
        if (type.startsWith("Array")) {
            type = type.replace(/^Array<(.+)>$/, "$1")
        } else {
            throw new Error(`Unhandled vararg declaration for ${name} parameter`)
        }
    }

    return `${node.dotDotDotToken ? "vararg " : ""}${name}: ${type}${node.questionToken ? " = definedExternally" : ""}`
})

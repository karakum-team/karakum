import ts from "typescript";
import {ConverterPlugin} from "../plugin";

export const convertParameterDeclaration: ConverterPlugin = (node, context, render) => {
    if (!ts.isParameter(node)) return null
    context.cover(node)
    node.dotDotDotToken && context.cover(node.dotDotDotToken)
    node.questionToken && context.cover(node.questionToken)

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
}

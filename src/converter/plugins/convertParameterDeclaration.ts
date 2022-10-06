import ts, {ParameterDeclaration, TypeNode} from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {ConverterContext} from "../context";
import {Render} from "../render";

export const convertParameterDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isParameter(node)) return null

    return convertParameterDeclarationWithFixedType(node, node.type, false, context, render)
})

export const convertParameterDeclarationWithFixedType = (
    node: ParameterDeclaration,
    type: TypeNode | undefined,
    nullable: boolean,
    context: ConverterContext,
    render: Render
) => {
    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)
    node.dotDotDotToken && checkCoverageService?.cover(node.dotDotDotToken)
    node.questionToken && checkCoverageService?.cover(node.questionToken)

    const name = render(node.name)

    let renderedType = type && render(type)

    if (!renderedType) {
        // throw new Error(`${name} parameter declaration without type is unsupported`)
        renderedType = "Any? /* some expression */" // TODO: resolve types
    }

    if (node.dotDotDotToken) {
        if (renderedType.startsWith("Array")) {
            renderedType = renderedType.replace(/^Array<(.+)>$/, "$1")
        } else {
            throw new Error(`Unhandled vararg declaration for ${name} parameter`)
        }
    }

    return `${node.dotDotDotToken ? "vararg " : ""}${name}: ${renderedType}${nullable ? "?" : ""}${node.questionToken ? " = definedExternally" : ""}`
}

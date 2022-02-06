import ts from "typescript";
import {ConverterPlugin} from "../plugin";
import {ifPresent} from "../render";

export const convertMethodSignature: ConverterPlugin = (node, context, render) => {
    if (!ts.isMethodSignature(node)) return null
    context.cover(node)

    const name = render(node.name)

    const typeParameters = node.typeParameters
        ?.map(typeParameter => render(typeParameter))
        ?.join(", ")

    const parameters = node.parameters
        ?.map(heritageClause => render(heritageClause))
        ?.join(", ")

    const returnType = node.type && render(node.type)

    return `fun ${ifPresent(typeParameters, it => `<${it}> `)}${name}(${parameters})${ifPresent(returnType, it => `: ${it}`)}`
}

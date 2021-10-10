import ts from "typescript";
import {ConverterPlugin} from "../plugin";
import {ifPresent} from "../render";

export const convertCallSignature: ConverterPlugin = (node, context, render) => {
    if (!ts.isCallSignatureDeclaration(node)) return null
    context.cover(node)

    const typeParameters = node.typeParameters
        ?.map(typeParameter => render(typeParameter))
        ?.join(", ")

    const parameters = node.parameters
        ?.map(heritageClause => render(heritageClause))
        ?.join(", ")

    const returnType = node.type && render(node.type)

    return `fun ${ifPresent(typeParameters, it => `<${it}>`)} invoke(${parameters})${ifPresent(returnType, it => `: ${it}`)}`
}

import ts, {SyntaxKind} from "typescript";
import {ConverterPlugin} from "../plugin";
import {ifPresent} from "../render";

export const convertFunctionDeclaration: ConverterPlugin = (node, context, render) => {
    if (!ts.isFunctionDeclaration(node)) return null
    context.cover(node)

    const exportModifier = node.modifiers?.find(it => it.kind === SyntaxKind.ExportKeyword)
    exportModifier && context.cover(exportModifier)

    // skip body
    node.body && context.cover(node.body)

    const name = (node.name && render(node.name)) ?? "Anonymous"

    const typeParameters = node.typeParameters
        ?.map(typeParameter => render(typeParameter))
        ?.join(", ")

    const parameters = node.parameters
        ?.map(heritageClause => render(heritageClause))
        ?.join(", ")

    const returnType = node.type && render(node.type)

    return `external fun ${ifPresent(typeParameters, it => `<${it}> `)}${name}(${parameters})${ifPresent(returnType, it => `: ${it}`)}`
}

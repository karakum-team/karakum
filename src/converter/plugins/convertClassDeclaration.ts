import ts, {SyntaxKind} from "typescript";
import {ConverterPlugin} from "../plugin";
import {ifPresent} from "../render";

export const convertClassDeclaration: ConverterPlugin = (node, context, render) => {
    if (!ts.isClassDeclaration(node)) return null
    context.cover(node)

    const exportModifier = node.modifiers?.find(it => it.kind === SyntaxKind.ExportKeyword)
    exportModifier && context.cover(exportModifier)

    const name = (node.name && render(node.name)) ?? "Anonymous"

    const typeParameters = node.typeParameters
        ?.map(typeParameter => render(typeParameter))
        ?.join(", ")

    const heritageClauses = node.heritageClauses
        ?.map(heritageClause => render(heritageClause))
        ?.join(" ")

    const members = node.members
        .map(member => render(member))
        .join("\n")

    return `
external class ${name} ${ifPresent(typeParameters, it => `<${it}> `)}${heritageClauses ?? ""} {
${members}
}
    `
}

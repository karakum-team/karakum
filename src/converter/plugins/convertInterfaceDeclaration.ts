import ts, {SyntaxKind} from "typescript";
import {ConverterPlugin} from "../plugin";
import {ifPresent} from "../render";

export const convertInterfaceDeclaration: ConverterPlugin = (node, context, render) => {
    if (!ts.isInterfaceDeclaration(node)) return null
    context.cover(node)

    const exportModifier = node.modifiers?.find(it => it.kind === SyntaxKind.ExportKeyword)
    exportModifier && context.cover(exportModifier)

    const name = render(node.name)

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
external interface ${name} ${ifPresent(typeParameters, it => `<${it}> `)}${heritageClauses ?? ""} {
${members}
}
    `
}

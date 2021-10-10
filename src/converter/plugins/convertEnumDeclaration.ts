import ts from "typescript";
import {ConverterPlugin} from "../plugin";

export const convertEnumDeclaration: ConverterPlugin = (node, context, render) => {
    if (!ts.isEnumDeclaration(node)) return null
    context.cover(node)

    const name = render(node.name)

    const members = node.members
        .map(member => render(member))
        .join(",\n")

    return `
        external enum class ${name} {
            ${members}
        }
    `
}

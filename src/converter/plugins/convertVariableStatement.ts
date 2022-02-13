import ts, {SyntaxKind} from "typescript";
import {ConverterPlugin} from "../plugin";

export const convertVariableStatement: ConverterPlugin = (node, context, render) => {
    if (!ts.isVariableStatement(node)) return null
    context.cover(node)
    context.cover(node.declarationList)

    const exportModifier = node.modifiers?.find(it => it.kind === SyntaxKind.ExportKeyword)
    exportModifier && context.cover(exportModifier)

    return node.declarationList.declarations
        .map(declaration => render(declaration))
        .join("\n")
}

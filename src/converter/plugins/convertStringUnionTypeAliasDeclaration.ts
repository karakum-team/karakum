import ts, {LiteralTypeNode, StringLiteral, SyntaxKind} from "typescript";
import {createSimplePlugin} from "../plugin";
import {identifier} from "../../utils/strings";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertStringUnionTypeAliasDeclaration = createSimplePlugin((node, context, render) => {
    if (
        ts.isTypeAliasDeclaration(node)
        && ts.isUnionTypeNode(node.type)
        && node.type.types.every(type => (
            ts.isLiteralTypeNode(type)
            && ts.isStringLiteral(type.literal)
        ))
    ) {
        const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
        checkCoverageService?.cover(node)
        checkCoverageService?.cover(node.type)

        const exportModifier = node.modifiers?.find(it => it.kind === SyntaxKind.ExportKeyword)
        exportModifier && checkCoverageService?.cover(exportModifier)

        const declareModifier = node.modifiers?.find(it => it.kind === SyntaxKind.DeclareKeyword)
        declareModifier && checkCoverageService?.cover(declareModifier)

        const entries = node.type.types
            .filter((type): type is LiteralTypeNode => ts.isLiteralTypeNode(type))
            .map(type => {
                checkCoverageService?.cover(type)

                return type.literal
            })
            .filter((literal): literal is StringLiteral => ts.isStringLiteral(literal))
            .map(literal => {
                checkCoverageService?.cover(literal)

                const value = literal.text
                const key = identifier(value)
                return [key, value] as const
            })

        const name = render(node.name)

        const keys = entries.map(([key]) => key)

        const body = keys.join(",\n")

        const jsName = entries
            .map(([key, value]) => `${key}: '${value}'`)
            .join(", ")

        return `
@JsName("""(/*union*/{${jsName}}/*union*/)""")
external enum class ${name} {
${body},

    ;
}
        `
    }

    return null;
})

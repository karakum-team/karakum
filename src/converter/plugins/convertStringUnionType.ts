import ts, {LiteralTypeNode, StringLiteral} from "typescript";
import {createSimplePlugin} from "../plugin";
import {identifier} from "../../utils/strings";

export const convertStringUnionType = createSimplePlugin((node, context, render) => {
    if (
        ts.isTypeAliasDeclaration(node)
        && ts.isUnionTypeNode(node.type)
        && node.type.types.every(type => (
            ts.isLiteralTypeNode(type)
            && ts.isStringLiteral(type.literal)
        ))
    ) {
        const entries = node.type.types
            .filter((type): type is LiteralTypeNode => ts.isLiteralTypeNode(type))
            .map(type => type.literal)
            .filter((literal): literal is StringLiteral => ts.isStringLiteral(literal))
            .map(literal => {
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

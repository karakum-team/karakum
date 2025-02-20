import ts from "typescript";

export default {
    plugins: [
        node => {
            if (
                ts.isTypeReferenceNode(node)
                && ts.isIdentifier(node.typeName)
                && node.typeName.text === "Error"
            ) {
                return "js.errors.JsError"
            }
            if (
                ts.isExpressionWithTypeArguments(node)
                && ts.isIdentifier(node.expression)
                && node.expression.text === "Error"
            ) {
                return "js.errors.JsError"
            }
            return null
        }
    ],
    nameResolvers: [
        node => {
            if (
                ts.isLiteralTypeNode(node)
                && ts.isStringLiteral(node.literal)

                && node.parent
                && ts.isUnionTypeNode(node.parent)

                && node.parent.parent
                && ts.isTypeAliasDeclaration(node.parent.parent)
                && node.parent.parent.name.text === "UnionWithDuplicatesAndResolution"
            ) {
                return "stringTrue"
            }
            return null
        }
    ]
}

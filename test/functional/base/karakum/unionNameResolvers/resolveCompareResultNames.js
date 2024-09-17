import ts from "typescript";

export default (node) => {
    if (!ts.isNumericLiteral(node) &&
        !(ts.isPrefixUnaryExpression(node) && ts.isNumericLiteral(node.operand))) return null
    if (!ts.isLiteralTypeNode(node.parent)) return null
    if (!ts.isUnionTypeNode(node.parent.parent)) return null
    if (!ts.isTypeAliasDeclaration(node.parent.parent.parent)) return null
    if (!ts.isIdentifier(node.parent.parent.parent.name)) return null

    const name = node.parent.parent.parent.name.text
    if (name === "AnotherCompareResult2") {
        return "RESULT"
    }

    return null
}

import ts from "typescript";

export default (node) => {
    if (!ts.isStringLiteral(node)) return null
    if (!ts.isLiteralTypeNode(node.parent)) return null
    if (!ts.isUnionTypeNode(node.parent.parent)) return null
    if (!ts.isTypeAliasDeclaration(node.parent.parent.parent)) return null
    if (!ts.isIdentifier(node.parent.parent.parent.name)) return null

    const name = node.parent.parent.parent.name.text
    if (name === "Operator2") {
        switch (node.text) {
            case "":
                return "EMPTY"
            case "=":
                return "EQUAL"
            case "<":
                return "LT"
            case ">":
                return "GT"
            case "<=":
                return "LTE"
        }
    } else if (name === "Operator3") {
        return "OPERATOR"
    }

    if (name.startsWith("Operator") && (node.text === ">=")) {
        return "GTE"
    }

    return null
}

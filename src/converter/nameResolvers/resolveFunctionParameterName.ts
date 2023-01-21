import ts, {TypeLiteralNode} from "typescript";
import {NameResolver} from "../nameResolver";
import {capitalize} from "../../utils/strings";

export const resolveFunctionParameterName: NameResolver<TypeLiteralNode> = (node) => {
    if (!ts.isParameter(node.parent)) return null
    if (!ts.isIdentifier(node.parent.name)) return null

    const parameterName = node.parent.name.text

    if (!ts.isFunctionTypeNode(node.parent.parent)) return null
    if (!ts.isTypeAliasDeclaration(node.parent.parent.parent)) return null

    const parentName = node.parent.parent.parent.name.text

    return `${capitalize(parentName)}${capitalize(parameterName)}`
}

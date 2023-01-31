import ts, {TypeLiteralNode} from "typescript";
import {NameResolver} from "../nameResolver";
import {capitalize} from "../../utils/strings";

export const resolveFunctionParameterName: NameResolver<TypeLiteralNode> = (node) => {
    if (!node.parent) return null
    if (!ts.isParameter(node.parent)) return null
    if (!ts.isIdentifier(node.parent.name)) return null

    const parameterName = node.parent.name.text

    if (!node.parent.parent) return null
    if (!ts.isFunctionDeclaration(node.parent.parent)) return null
    if (node.parent.parent.name === undefined) return null

    const parentName = node.parent.parent.name.text

    return `${capitalize(parentName)}${capitalize(parameterName)}`
}

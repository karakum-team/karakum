import ts, {TypeLiteralNode} from "typescript";
import {NameResolver} from "../nameResolver";
import {capitalize} from "../../utils/strings";

export const resolveConstructorParameterName: NameResolver<TypeLiteralNode> = (node) => {
    if (!node.parent) return null
    if (!ts.isParameter(node.parent)) return null
    if (!ts.isIdentifier(node.parent.name)) return null

    const parameterName = node.parent.name.text

    if (!node.parent.parent) return null
    if (!ts.isConstructorDeclaration(node.parent.parent)) return null
    if (!node.parent.parent.parent) return null
    if (node.parent.parent.parent.name === undefined) return null

    const parentName = node.parent.parent.parent.name.text

    return `${capitalize(parentName)}${capitalize(parameterName)}`
}

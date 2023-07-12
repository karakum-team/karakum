import ts from "typescript";
import {NameResolver} from "../nameResolver.js";
import {capitalize} from "../../utils/strings.js";

export const resolveFunctionParameterName: NameResolver = (node) => {
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

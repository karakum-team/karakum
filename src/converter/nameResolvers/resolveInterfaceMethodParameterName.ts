import ts from "typescript";
import {NameResolver} from "../nameResolver";
import {capitalize} from "../../utils/strings";

export const resolveInterfaceMethodParameterName: NameResolver = (node) => {
    if (!node.parent) return null
    if (!ts.isParameter(node.parent)) return null
    if (!ts.isIdentifier(node.parent.name)) return null

    const parameterName = node.parent.name.text

    if (!node.parent.parent) return null
    if (!ts.isMethodSignature(node.parent.parent)) return null
    if (!ts.isIdentifier(node.parent.parent.name)) return null

    const methodName = node.parent.parent.name.text

    if (!node.parent.parent.parent) return null
    if (!ts.isInterfaceDeclaration(node.parent.parent.parent)) return null
    if (node.parent.parent.parent.name === undefined) return null

    const parentName = node.parent.parent.parent.name.text

    return `${capitalize(parentName)}${capitalize(methodName)}${capitalize(parameterName)}`
}

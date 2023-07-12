import ts from "typescript";
import {NameResolver} from "../nameResolver.js";
import {capitalize} from "../../utils/strings.js";

export const resolveClassMethodReturnTypeName: NameResolver = (node) => {
    if (!node.parent) return null
    if (!ts.isMethodDeclaration(node.parent)) return null
    if (!ts.isIdentifier(node.parent.name)) return null
    if (node.parent.type !== node) return null

    const methodName = node.parent.name.text

    if (!node.parent.parent) return null
    if (!ts.isClassDeclaration(node.parent.parent)) return null
    if (node.parent.parent.name === undefined) return null

    const parentName = node.parent.parent.name.text

    return `${capitalize(parentName)}${capitalize(methodName)}Result`
}

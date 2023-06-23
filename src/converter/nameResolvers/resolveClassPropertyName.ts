import ts from "typescript";
import {NameResolver} from "../nameResolver";
import {capitalize} from "../../utils/strings";

export const resolveClassPropertyName: NameResolver = (node) => {
    if (!node.parent) return null
    if (!ts.isPropertyDeclaration(node.parent)) return null
    if (!ts.isIdentifier(node.parent.name)) return null

    const propertyName = node.parent.name.text

    if (!node.parent.parent) return null
    if (!ts.isClassDeclaration(node.parent.parent)) return null
    if (node.parent.parent.name === undefined) return null

    const parentName = node.parent.parent.name.text

    return `${capitalize(parentName)}${capitalize(propertyName)}`
}

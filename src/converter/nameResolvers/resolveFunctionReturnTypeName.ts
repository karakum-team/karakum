import ts from "typescript";
import {NameResolver} from "../nameResolver.js";
import {capitalize} from "../../utils/strings.js";

export const resolveFunctionReturnTypeName: NameResolver = (node) => {
    if (!node.parent) return null
    if (!ts.isFunctionDeclaration(node.parent)) return null
    if (node.parent.name === undefined) return null
    if (node.parent.type !== node) return null

    const parentName = node.parent.name.text

    return `${capitalize(parentName)}Result`
}

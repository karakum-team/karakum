import ts, {TypeLiteralNode} from "typescript";
import {NameResolver} from "../nameResolver";
import {capitalize} from "../../utils/strings";

export const resolveFunctionReturnTypeName: NameResolver<TypeLiteralNode> = (node) => {
    if (!ts.isFunctionDeclaration(node.parent)) return null
    if (node.parent.name === undefined) return null
    if (node.parent.type !== node) return null

    const parentName = node.parent.name.text

    return `${capitalize(parentName)}Result`
}

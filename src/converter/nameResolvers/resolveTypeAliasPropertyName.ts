import ts, {TypeLiteralNode} from "typescript";
import {NameResolver} from "../nameResolver";
import {capitalize} from "../../utils/strings";

export const resolveTypeAliasPropertyName: NameResolver<TypeLiteralNode> = (node) => {
    if (!ts.isPropertySignature(node.parent)) return null
    if (!ts.isIdentifier(node.parent.name)) return null

    const propertyName = node.parent.name.text

    if (!ts.isTypeLiteralNode(node.parent.parent)) return null
    if (!ts.isTypeAliasDeclaration(node.parent.parent.parent)) return null

    const parentName = node.parent.parent.parent.name.text

    return `${capitalize(parentName)}${capitalize(propertyName)}`
}

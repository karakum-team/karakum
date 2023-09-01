import ts from "typescript";
import {NameResolver} from "../nameResolver.js";
import {capitalize} from "../../utils/strings.js";
import {TypeScriptService, typeScriptServiceKey} from "../plugins/TypeScriptPlugin.js";

export const resolveTypeAliasPropertyName: NameResolver = (node, context) => {
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    const getParent = typeScriptService?.getParent.bind(typeScriptService) ?? ((node: ts.Node) => node.parent)

    const property = getParent(node)
    if (!property) return null
    if (!ts.isPropertySignature(property)) return null
    if (!ts.isIdentifier(property.name)) return null

    const propertyName = property.name.text

    const typeLiteral = getParent(property)
    if (!typeLiteral) return null
    if (!ts.isTypeLiteralNode(typeLiteral)) return null

    const typeAlias = getParent(typeLiteral)
    if (!typeAlias) return null
    if (!ts.isTypeAliasDeclaration(typeAlias)) return null

    const parentName = typeAlias.name.text

    return `${capitalize(parentName)}${capitalize(propertyName)}`
}

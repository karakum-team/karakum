import ts from "typescript";
import {NameResolver} from "../nameResolver.js";
import {capitalize} from "../../utils/strings.js";
import {TypeScriptService, typeScriptServiceKey} from "../plugins/TypeScriptPlugin.js";

export const resolveFunctionTypeAliasParameterName: NameResolver = (node, context) => {
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    const getParent = typeScriptService?.getParent.bind(typeScriptService) ?? ((node: ts.Node) => node.parent)

    const parameter = getParent(node)
    if (!parameter) return null
    if (!ts.isParameter(parameter)) return null
    if (!ts.isIdentifier(parameter.name)) return null

    const parameterName = parameter.name.text

    const functionType = getParent(parameter)
    if (!functionType) return null
    if (!ts.isFunctionTypeNode(functionType)) return null

    const typeAlias = getParent(functionType)
    if (!typeAlias) return null
    if (!ts.isTypeAliasDeclaration(typeAlias)) return null

    const parentName = typeAlias.name.text

    return `${capitalize(parentName)}${capitalize(parameterName)}`
}

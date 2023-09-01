import ts from "typescript";
import {NameResolver} from "../nameResolver.js";
import {capitalize} from "../../utils/strings.js";
import {TypeScriptService, typeScriptServiceKey} from "../plugins/TypeScriptPlugin.js";

export const resolveFunctionParameterName: NameResolver = (node, context) => {
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    const getParent = typeScriptService?.getParent.bind(typeScriptService) ?? ((node: ts.Node) => node.parent)

    const parameter = getParent(node)
    if (!parameter) return null
    if (!ts.isParameter(parameter)) return null
    if (!ts.isIdentifier(parameter.name)) return null

    const parameterName = parameter.name.text

    const functionNode = getParent(parameter)
    if (!functionNode) return null
    if (!ts.isFunctionDeclaration(functionNode)) return null
    if (functionNode.name === undefined) return null

    const parentName = functionNode.name.text

    return `${capitalize(parentName)}${capitalize(parameterName)}`
}

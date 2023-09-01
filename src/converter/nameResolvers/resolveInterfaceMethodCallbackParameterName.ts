import ts from "typescript";
import {NameResolver} from "../nameResolver.js";
import {capitalize} from "../../utils/strings.js";
import {TypeScriptService, typeScriptServiceKey} from "../plugins/TypeScriptPlugin.js";

export const resolveInterfaceMethodCallbackParameterName: NameResolver = (node, context) => {
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    const getParent = typeScriptService?.getParent.bind(typeScriptService) ?? ((node: ts.Node) => node.parent)

    const callbackParameter = getParent(node)
    if (!callbackParameter) return null
    if (!ts.isParameter(callbackParameter)) return null
    if (!ts.isIdentifier(callbackParameter.name)) return null

    const callbackParameterName = callbackParameter.name.text

    const functionType = getParent(callbackParameter)
    if (!functionType) return null
    if (!ts.isFunctionTypeNode(functionType)) return null

    const parameter = getParent(functionType)
    if (!parameter) return null
    if (!ts.isParameter(parameter)) return null
    if (!ts.isIdentifier(parameter.name)) return null

    const parameterName = parameter.name.text

    const method = getParent(parameter)
    if (!method) return null
    if (!ts.isMethodSignature(method)) return null
    if (!ts.isIdentifier(method.name)) return null

    const methodName = method.name.text

    const interfaceNode = getParent(method)
    if (!interfaceNode) return null
    if (!ts.isInterfaceDeclaration(interfaceNode)) return null

    const parentName = interfaceNode.name.text

    return `${capitalize(parentName)}${capitalize(methodName)}${capitalize(parameterName)}${capitalize(callbackParameterName)}`
}

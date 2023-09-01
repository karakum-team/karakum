import ts from "typescript";
import {NameResolver} from "../nameResolver.js";
import {capitalize} from "../../utils/strings.js";
import {TypeScriptService, typeScriptServiceKey} from "../plugins/TypeScriptPlugin.js";

export const resolveClassPropertyCallbackParameterName: NameResolver = (node, context) => {
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

    const property = getParent(functionType)
    if (!property) return null
    if (!ts.isPropertyDeclaration(property)) return null
    if (!ts.isIdentifier(property.name)) return null

    const propertyName = property.name.text

    const classNode = getParent(property)
    if (!classNode) return null
    if (!ts.isClassDeclaration(classNode)) return null
    if (classNode.name === undefined) return null

    const parentName = classNode.name.text

    return `${capitalize(parentName)}${capitalize(propertyName)}${capitalize(callbackParameterName)}`
}

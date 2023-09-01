import ts from "typescript";
import {NameResolver} from "../nameResolver.js";
import {capitalize} from "../../utils/strings.js";
import {TypeScriptService, typeScriptServiceKey} from "../plugins/TypeScriptPlugin.js";

export const resolveInterfaceMethodReturnTypeName: NameResolver = (node, context) => {
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    const getParent = typeScriptService?.getParent.bind(typeScriptService) ?? ((node: ts.Node) => node.parent)

    const method = getParent(node)
    if (!method) return null
    if (!ts.isMethodSignature(method)) return null
    if (!ts.isIdentifier(method.name)) return null
    if (method.type !== node) return null

    const methodName = method.name.text

    const interfaceNode = getParent(method)
    if (!interfaceNode) return null
    if (!ts.isInterfaceDeclaration(interfaceNode)) return null

    const parentName = interfaceNode.name.text

    return `${capitalize(parentName)}${capitalize(methodName)}Result`
}

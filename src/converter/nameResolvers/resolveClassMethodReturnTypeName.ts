import ts from "typescript";
import {NameResolver} from "../nameResolver.js";
import {capitalize} from "../../utils/strings.js";
import {TypeScriptService, typeScriptServiceKey} from "../plugins/TypeScriptPlugin.js";

export const resolveClassMethodReturnTypeName: NameResolver = (node, context) => {
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    const getParent = typeScriptService?.getParent.bind(typeScriptService) ?? ((node: ts.Node) => node.parent)

    const method = getParent(node)
    if (!method) return null
    if (!ts.isMethodDeclaration(method)) return null
    if (!ts.isIdentifier(method.name)) return null
    if (method.type !== node) return null

    const methodName = method.name.text

    const classNode = getParent(method)
    if (!classNode) return null
    if (!ts.isClassDeclaration(classNode)) return null
    if (classNode.name === undefined) return null

    const parentName = classNode.name.text

    return `${capitalize(parentName)}${capitalize(methodName)}Result`
}

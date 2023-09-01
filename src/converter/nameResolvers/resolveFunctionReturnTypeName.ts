import ts from "typescript";
import {NameResolver} from "../nameResolver.js";
import {capitalize} from "../../utils/strings.js";
import {TypeScriptService, typeScriptServiceKey} from "../plugins/TypeScriptPlugin.js";

export const resolveFunctionReturnTypeName: NameResolver = (node, context) => {
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    const getParent = typeScriptService?.getParent.bind(typeScriptService) ?? ((node: ts.Node) => node.parent)

    const functionNode = getParent(node)
    if (!functionNode) return null
    if (!ts.isFunctionDeclaration(functionNode)) return null
    if (functionNode.name === undefined) return null
    if (functionNode.type !== node) return null

    const parentName = functionNode.name.text

    return `${capitalize(parentName)}Result`
}

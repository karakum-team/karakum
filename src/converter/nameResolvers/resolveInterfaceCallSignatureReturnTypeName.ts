import ts from "typescript";
import {NameResolver} from "../nameResolver.js";
import {capitalize} from "../../utils/strings.js";
import {TypeScriptService, typeScriptServiceKey} from "../plugins/TypeScriptPlugin.js";

export const resolveInterfaceCallSignatureReturnTypeName: NameResolver = (node, context) => {
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    const getParent = typeScriptService?.getParent.bind(typeScriptService) ?? ((node: ts.Node) => node.parent)

    const callSignature = getParent(node)
    if (!callSignature) return null
    if (!ts.isCallSignatureDeclaration(callSignature)) return null
    if (callSignature.type !== node) return null

    const interfaceNode = getParent(callSignature)
    if (!interfaceNode) return null
    if (!ts.isInterfaceDeclaration(interfaceNode)) return null

    const parentName = interfaceNode.name.text

    return `${capitalize(parentName)}Result`
}

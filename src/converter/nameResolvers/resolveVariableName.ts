import ts from "typescript";
import {NameResolver} from "../nameResolver.js";
import {capitalize} from "../../utils/strings.js";
import {TypeScriptService, typeScriptServiceKey} from "../plugins/TypeScriptPlugin.js";

export const resolveVariableName: NameResolver = (node, context) => {
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    const getParent = typeScriptService?.getParent.bind(typeScriptService) ?? ((node: ts.Node) => node.parent)

    const variable = getParent(node)
    if (!variable) return null
    if (!ts.isVariableDeclaration(variable)) return null
    if (!ts.isIdentifier(variable.name)) return null

    const variableName = variable.name.text

    return capitalize(variableName)
}

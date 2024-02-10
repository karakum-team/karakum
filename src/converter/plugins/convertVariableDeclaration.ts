import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";
import {NamespaceInfoService, namespaceInfoServiceKey} from "./NamespaceInfoPlugin.js";

export const convertVariableDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isVariableDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    // skip initializer
    node.initializer && checkCoverageService?.cover(node.initializer)

    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    const namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)

    const modifier = node.parent.flags & ts.NodeFlags.Const
        ? "val "
        : "var "

    const name = render(node.name)

    const namespace = typeScriptService?.findClosest(node, ts.isModuleDeclaration)

    let externalModifier = "external "

    if (namespace !== undefined && namespaceInfoService?.resolveNamespaceStrategy(namespace) === "object") {
        externalModifier = ""
    }

    let type: string

    if (node.type) {
        type = render(node.type)
    } else {
        type = "Any? /* should be inferred */" // TODO: infer types
    }

    return `${externalModifier}${modifier}${name}: ${type}`
})

import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {NamespaceInfoService, namespaceInfoServiceKey} from "./NamespaceInfoPlugin.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";

export const convertModuleDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isModuleDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    const namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)

    const namespaceStrategy = namespaceInfoService?.resolveNamespaceStrategy(node)

    if (namespaceStrategy === "ignore") {
        return (node.body && render(node.body)) ?? ""
    }

    if (namespaceStrategy === "object") {
        const name = render(node.name)

        const namespace = typeScriptService?.findClosest(node.parent, ts.isModuleDeclaration)

        let externalModifier = "external "

        if (namespace !== undefined && namespaceInfoService?.resolveNamespaceStrategy(namespace) === "object") {
            externalModifier = ""
        }

        const body = (node.body && render(node.body)) ?? ""

        return `
${externalModifier}object ${name} {
${body}
}
        `
    }

    if (namespaceStrategy === "package") {
        // was handled by file structure
        return ""
    }

    throw new Error(`Unknown namespace strategy: ${namespaceStrategy}`)
})

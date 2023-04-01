import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {NamespaceInfoService, namespaceInfoServiceKey} from "./NamespaceInfoPlugin";

export const convertModuleDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isModuleDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    const namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)

    checkCoverageService?.cover(node)

    const namespaceStrategy = namespaceInfoService?.resolveNamespaceStrategy(node)

    if (namespaceStrategy === "ignore") {
        return (node.body && render(node.body)) ?? ""
    }

    if (namespaceStrategy === "object") {
        const name = render(node.name)

        const body = (node.body && render(node.body)) ?? ""

        return `
external object ${name} {
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

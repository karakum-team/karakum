import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";

export const convertNamedTupleMember = createSimplePlugin((node, context, render) => {
    if (!ts.isNamedTupleMember(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)

    checkCoverageService?.cover(node)

    const name = render(node.name)
    const type = render(node.type)

    return `/* ${name}: */ ${type}`
})

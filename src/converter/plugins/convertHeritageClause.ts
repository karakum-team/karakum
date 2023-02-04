import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertHeritageClause = createSimplePlugin((node, context, render) => {
    if (!ts.isHeritageClause(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    return node.types
        .map(type => render(type))
        .join(", ")
})

import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";

export const convertIndexedAccessType = createSimplePlugin((node, context, render) => {
    if (!ts.isIndexedAccessTypeNode(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    checkCoverageService?.deepCover(node)

    const resolvedType = typeScriptService?.resolveType(node)

    if (!resolvedType) return `Any /* ${typeScriptService?.printNode(node)} */`;

    return render(resolvedType)
})

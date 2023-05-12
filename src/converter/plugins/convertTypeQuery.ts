import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertTypeQuery = createSimplePlugin((node, context, render) => {
    if (!ts.isTypeQueryNode(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    checkCoverageService?.deepCover(node)

    const resolvedType = typeScriptService?.resolveType(node)

    if (!resolvedType) return `Any /* ${typeScriptService?.printNode(node)} */`;

    return render(resolvedType)
})

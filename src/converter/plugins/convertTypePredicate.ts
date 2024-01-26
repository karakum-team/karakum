import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";

export const convertTypePredicate = createSimplePlugin((node, context, render) => {
    if (!ts.isTypePredicateNode(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.deepCover(node)

    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    // TODO: support contracts

    const type = Boolean(node.assertsModifier)
        ? "Unit"
        : "Boolean"

    return `${type} /* ${typeScriptService?.printNode(node)} */`
})

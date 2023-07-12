import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";

export const convertTypePredicate = createSimplePlugin((node, context, render) => {
    if (!ts.isTypePredicateNode(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.deepCover(node)

    // TODO: support contracts

    return "Boolean"
})

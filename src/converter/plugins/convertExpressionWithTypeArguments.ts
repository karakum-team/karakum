import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {convertNodeWithTypeArguments} from "./convertNodeWithTypeArguments.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";

export const convertExpressionWithTypeArguments = createSimplePlugin((node, context, render) => {
    if (!ts.isExpressionWithTypeArguments(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    return render(node.expression) + convertNodeWithTypeArguments.render(node, context, render)
})

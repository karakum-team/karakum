import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {convertNodeWithTypeArguments} from "./convertNodeWithTypeArguments";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertExpressionWithTypeArguments = createSimplePlugin((node, context, render) => {
    if (!ts.isExpressionWithTypeArguments(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    return render(node.expression) + convertNodeWithTypeArguments.render(node, context, render)
})

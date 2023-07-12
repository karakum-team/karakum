import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {convertNodeWithTypeArguments} from "./convertNodeWithTypeArguments.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";

export const convertTypeReference = createSimplePlugin((node, context, render) => {
    if (!ts.isTypeReferenceNode(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    return render(node.typeName) + convertNodeWithTypeArguments.render(node, context, render)
})

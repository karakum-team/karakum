import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {convertNodeWithTypeArguments} from "./convertNodeWithTypeArguments";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertTypeReferenceNode = createSimplePlugin((node, context, render) => {
    if (!ts.isTypeReferenceNode(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    return render(node.typeName) + convertNodeWithTypeArguments.render(node, context, render)
})

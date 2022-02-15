import ts, {SyntaxKind} from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertReadonlyArrayType = createSimplePlugin((node, context, render) => {
    if (
        ts.isTypeOperatorNode(node) &&
        node.operator === SyntaxKind.ReadonlyKeyword &&
        ts.isArrayTypeNode(node.type)
    ) {

        const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
        checkCoverageService?.cover(node)
        checkCoverageService?.cover(node.type)

        return `Array<out ${render(node.type.elementType)}>`
    }



    return null;
})

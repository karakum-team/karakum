import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertFunctionType = createSimplePlugin((node, context, render) => {
    if (!ts.isFunctionTypeNode(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    if(node.typeParameters) {
        // TODO: supports generics
        return "(vararg args: Any?) -> Any?"
    }

    const parameters = node.parameters
        ?.map(parameter => render(parameter))
        ?.join(", ")

    const returnType = render(node.type)

    return `(${parameters}) -> ${returnType}`
})

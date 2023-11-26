import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";

export const convertFunctionType = createSimplePlugin((node, context, render) => {
    if (!ts.isFunctionTypeNode(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    checkCoverageService?.cover(node)

    if (node.typeParameters) {
        return `Function<Any?> /* ${typeScriptService?.printNode(node)} */`
    }

    const returnType = render(node.type)

    if (node.parameters.some(parameter => parameter.dotDotDotToken)) {
        return `Function<${returnType}> /* ${typeScriptService?.printNode(node)} */`
    }

    const parameters = node.parameters
        ?.map(parameter => render(parameter))
        ?.join(", ")

    return `(${parameters}) -> ${returnType}`
})

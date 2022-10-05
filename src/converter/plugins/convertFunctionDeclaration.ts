import ts, {SyntaxKind} from "typescript";
import {createSimplePlugin} from "../plugin";
import {ifPresent} from "../render";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";

export const convertFunctionDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isFunctionDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const exportModifier = node.modifiers?.find(it => it.kind === SyntaxKind.ExportKeyword)
    exportModifier && checkCoverageService?.cover(exportModifier)

    // skip body
    node.body && checkCoverageService?.cover(node.body)

    const name = (node.name && render(node.name)) ?? "Anonymous"

    const typeParameters = node.typeParameters
        ?.map(typeParameter => render(typeParameter))
        ?.join(", ")

    const parameters = node.parameters
        ?.map(parameter => render(parameter))
        ?.join(", ")

    const returnType = node.type && render(node.type)

    return `external fun ${ifPresent(typeParameters, it => `<${it}> `)}${name}(${parameters})${ifPresent(returnType, it => `: ${it}`)}`
})

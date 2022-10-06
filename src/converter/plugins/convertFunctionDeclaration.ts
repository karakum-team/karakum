import ts, {SyntaxKind} from "typescript";
import {createSimplePlugin} from "../plugin";
import {ifPresent} from "../render";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {prepareParameters} from "./prepareParameters";
import {convertParameterDeclarationWithFixedType} from "./convertParameterDeclaration";

export const convertFunctionDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isFunctionDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const exportModifier = node.modifiers?.find(it => it.kind === SyntaxKind.ExportKeyword)
    exportModifier && checkCoverageService?.cover(exportModifier)

    const declareModifier = node.modifiers?.find(it => it.kind === SyntaxKind.DeclareKeyword)
    declareModifier && checkCoverageService?.cover(declareModifier)

    // skip body
    node.body && checkCoverageService?.cover(node.body)

    const name = (node.name && render(node.name)) ?? "Anonymous"

    const typeParameters = node.typeParameters
        ?.map(typeParameter => render(typeParameter))
        ?.join(", ")

    const signatures = prepareParameters(node, context)

    const returnType = node.type && render(node.type)

    return signatures
        .map(signature => {
            const parameters = signature
                .map(({ parameter, type, nullable}) => {
                    return convertParameterDeclarationWithFixedType(parameter, type, nullable, context, render);
                })
                .join(", ")

            return `external fun ${ifPresent(typeParameters, it => `<${it}> `)}${name}(${parameters})${ifPresent(returnType, it => `: ${it}`)}`
        })
        .join("\n\n")
})

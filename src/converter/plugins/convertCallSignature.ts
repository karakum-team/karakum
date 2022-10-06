import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {ifPresent} from "../render";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {prepareParameters} from "./prepareParameters";
import {convertParameterDeclarationWithFixedType} from "./convertParameterDeclaration";

export const convertCallSignature = createSimplePlugin((node, context, render) => {
    if (!ts.isCallSignatureDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

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

            return `fun ${ifPresent(typeParameters, it => `<${it}>`)} invoke(${parameters})${ifPresent(returnType, it => `: ${it}`)}`
        })
        .join("\n\n")
})

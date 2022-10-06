import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {prepareParameters} from "./prepareParameters";
import {convertParameterDeclarationWithFixedType} from "./convertParameterDeclaration";

export const convertConstructorDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isConstructorDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const signatures = prepareParameters(node, context)

    return signatures
        .map(signature => {
            const parameters = signature
                .map(({ parameter, type, nullable}) => {
                    return convertParameterDeclarationWithFixedType(parameter, type, nullable, context, render);
                })
                .join(", ")

            return `constructor (${parameters})`
        })
        .join("\n\n")
})

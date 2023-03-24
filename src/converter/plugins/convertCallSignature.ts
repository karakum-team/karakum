import ts from "typescript";
import {createSimplePlugin} from "../plugin";
import {ifPresent} from "../render";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {convertParameterDeclarations} from "./convertParameterDeclaration";

export const convertCallSignature = createSimplePlugin((node, context, render) => {
    if (!ts.isCallSignatureDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    const typeParameters = node.typeParameters
        ?.map(typeParameter => render(typeParameter))
        ?.join(", ")

    const returnType = node.type && render(node.type)

    return convertParameterDeclarations(node, context, render, {
        strategy: "function",
        template: parameters => {
            return `
@Suppress("DEPRECATION")
@nativeInvoke
operator fun ${ifPresent(typeParameters, it => `<${it}>`)} invoke(${parameters})${ifPresent(returnType, it => `: ${it}`)}
            `
        }
    })
})

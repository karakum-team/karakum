import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {ifPresent} from "../render.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {convertParameterDeclarations} from "./convertParameterDeclaration.js";
import {InheritanceModifierService, inheritanceModifierServiceKey} from "./InheritanceModifierPlugin.js";

export const convertCallSignatureDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isCallSignatureDeclaration(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)

    checkCoverageService?.cover(node)

    const inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)

    const typeParameters = node.typeParameters
        ?.map(typeParameter => render(typeParameter))
        ?.join(", ")

    const returnType = node.type && render(node.type)

    return convertParameterDeclarations(node, context, render, {
        strategy: "function",
        template: (parameters, signature) => {
            const inheritanceModifier = inheritanceModifierService?.resolveSignatureInheritanceModifier(node, signature, context)

            return `
@seskar.js.JsNative
${ifPresent(inheritanceModifier, it => `${it} `)}operator fun ${ifPresent(typeParameters, it => `<${it}>`)} invoke(${parameters})${ifPresent(returnType, it => `: ${it}`)}
            `
        }
    })
})

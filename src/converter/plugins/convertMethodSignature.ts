import ts from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {ifPresent} from "../render.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {convertParameterDeclarations} from "./convertParameterDeclaration.js";
import {escapeIdentifier} from "../../utils/strings.js";
import {InheritanceModifierService, inheritanceModifierServiceKey} from "./InheritanceModifierPlugin.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";

export const convertMethodSignature = createSimplePlugin((node, context, render) => {
    if (!ts.isMethodSignature(node)) return null

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    checkCoverageService?.cover(node)

    const inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)

    const name = escapeIdentifier(render(node.name))

    const typeParameters = node.typeParameters
        ?.map(typeParameter => render(typeParameter))
        ?.filter(Boolean)
        ?.join(", ")

    const returnType = node.type && render(node.type)

    if (node.questionToken) {
        return convertParameterDeclarations(node, context, render, {
            strategy: "lambda",
            template: (parameters, signature) => {
                const inheritanceModifier = inheritanceModifierService?.resolveSignatureInheritanceModifier(node, signature, context)

                let functionType: string

                if (node.typeParameters) {
                    functionType = `Function<Any?> /* ${typeScriptService?.printNode(node)} */`
                } else if (node.parameters.some(parameter => parameter.dotDotDotToken)) {
                    functionType = `Function<${returnType}> /* ${typeScriptService?.printNode(node)} */`
                } else {
                    functionType = `(${parameters}) -> ${returnType ?? "Any?"}`
                }

                return `${ifPresent(inheritanceModifier, it => `${it} `)}val ${name}: (${functionType})?`
            }
        })
    }

    return convertParameterDeclarations(node, context, render, {
        strategy: "function",
        template: (parameters, signature) => {
            const inheritanceModifier = inheritanceModifierService?.resolveSignatureInheritanceModifier(node, signature, context)

            return `${ifPresent(inheritanceModifier, it => `${it} `)}fun ${ifPresent(typeParameters, it => `<${it}> `)}${name}(${parameters})${ifPresent(returnType, it => `: ${it}`)}`
        }
    })
})

import ts, {ParameterDeclaration, SignatureDeclarationBase, TypeNode} from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {isNullableType, isNullableUnionType} from "./NullableUnionTypePlugin.js";
import {isStringUnionType} from "./StringUnionTypePlugin.js";
import {ConverterContext} from "../context.js";
import {Render, renderNullable} from "../render.js";
import {escapeIdentifier} from "../../utils/strings.js";

export interface ParameterDeclarationsConfiguration {
    strategy: "function" | "lambda",
    template: (parameters: string, signature: Signature) => string,
}

export interface ParameterDeclarationConfiguration {
    strategy: "function" | "lambda",
    type: TypeNode | undefined,
    nullable: boolean,
}

export interface ParameterInfo {
    parameter: ParameterDeclaration,
    type: TypeNode | undefined,
    nullable: boolean,
    optional: boolean,
}

export type Signature = ParameterInfo[]

export const convertParameterDeclaration = createSimplePlugin((node, context, render) => {
    if (!ts.isParameter(node)) return null

    return convertParameterDeclarationWithFixedType(node, context, render, {
        strategy: ts.isFunctionTypeNode(node.parent)
            ? "lambda"
            : "function",
        type: node.type,
        nullable: false
    })
})

export const convertParameterDeclarations = (
    node: SignatureDeclarationBase,
    context: ConverterContext,
    render: Render,
    configuration: ParameterDeclarationsConfiguration,
) => {
    const {strategy, template} = configuration
    const initialSignature = extractSignature(node)

    if (strategy === "function") {
        const signatures = expandUnions(initialSignature, context)

        return signatures
            .map(signature => {
                const parameters = signature
                    .map(({ parameter, type, nullable}) => {
                        return convertParameterDeclarationWithFixedType(parameter, context, render, {
                            strategy,
                            type,
                            nullable,
                        })
                    })
                    .join(", ")

                return template(parameters, signature)
            })
            .join("\n\n")
    }

    if (strategy === "lambda") {
        const parameters = node.parameters
            .map(parameter => {
                return convertParameterDeclarationWithFixedType(parameter, context, render, {
                    strategy,
                    type: parameter.type,
                    nullable: false,
                })
            })
            .join(", ")

        return template(parameters, initialSignature)
    }

    throw new Error(`Unknown parameter declaration strategy: ${strategy}`)
}

const convertParameterDeclarationWithFixedType = (
    node: ParameterDeclaration,
    context: ConverterContext,
    render: Render,
    configuration: ParameterDeclarationConfiguration,
) => {
    const {type, strategy} = configuration

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)

    checkCoverageService?.cover(node)
    node.dotDotDotToken && checkCoverageService?.cover(node.dotDotDotToken)
    node.questionToken && checkCoverageService?.cover(node.questionToken)

    let name: string

    if (ts.isIdentifier(node.name)) {
        name = escapeIdentifier(render(node.name))
    } else {
        const parameterIndex = node.parent.parameters.indexOf(node)

        const anonymousParameters = node.parent.parameters
            .filter(parameter => !ts.isIdentifier(parameter.name))

        name = anonymousParameters.length === 1
            ? "options"
            : `param${parameterIndex}`

        checkCoverageService?.deepCover(node.name)
    }

    const isOptional = strategy === "lambda" && Boolean(node.questionToken)

    const isDefinedExternally = strategy === "function" && Boolean(node.questionToken)

    let renderedType = renderNullable(type, isOptional || configuration.nullable, context, render)

    if (type && node.dotDotDotToken) {
        if (renderedType.startsWith("Array")) {
            renderedType = renderedType.replace(/^Array<(.+)>$/, "$1")
        } else {
            renderedType = `Any? /* ${renderedType} */`
        }
    }


    return `${node.dotDotDotToken ? "vararg " : ""}${name}: ${renderedType}${isOptional ? " /* use undefined for default */" : ""}${isDefinedExternally ? " = definedExternally" : ""}`
}

const extractSignature = (node: SignatureDeclarationBase) => {
    return node.parameters.map(it => ({
        parameter: it,
        type: it.type,
        nullable: false,
        optional: Boolean(it.questionToken)
    }))
}

const expandUnions = (
    signature: Signature,
    context: ConverterContext,
): Signature[] => {
    const currentSignatures = [signature]

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)

    for (let parameterIndex = 0; parameterIndex < signature.length; parameterIndex++) {
        for (let signatureIndex = 0; signatureIndex < currentSignatures.length; signatureIndex++) {
            const signature = currentSignatures[signatureIndex]
            const {parameter, type, optional} = signature[parameterIndex]

            if (type && isStringUnionType(type)) continue

            if (type && ts.isUnionTypeNode(type)) {
                checkCoverageService?.cover(type)

                const generatedSignatures: Signature[] = []

                const nullable = isNullableUnionType(type, context)

                for (const subtype of type.types) {
                    if (isNullableType(subtype)) {
                        checkCoverageService?.deepCover(type)
                        continue
                    }

                    const generatedSignature: Signature = [...signature]
                    const parameterInfo = {
                        parameter,
                        type: subtype,
                        nullable,
                        optional,
                    }
                    generatedSignature.splice(parameterIndex, 1, parameterInfo)
                    generatedSignatures.push(generatedSignature)
                }

                currentSignatures.splice(signatureIndex, 1, ...generatedSignatures)
            }
        }
    }

    return currentSignatures
}

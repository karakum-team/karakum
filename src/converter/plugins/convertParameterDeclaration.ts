import ts, {ParameterDeclaration, SignatureDeclarationBase, TypeNode} from "typescript";
import {createSimplePlugin} from "../plugin.js";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin.js";
import {flatUnionTypes, isNullableType, isNullableUnionType} from "./NullableUnionTypePlugin.js";
import {isNullableStringUnionType} from "./StringUnionTypePlugin.js";
import {ConverterContext} from "../context.js";
import {Render, renderNullable} from "../render.js";
import {escapeIdentifier} from "../../utils/strings.js";
import {AnnotationService, annotationServiceKey} from "./AnnotationPlugin.js";
import {InheritanceModifierService, inheritanceModifierServiceKey} from "./InheritanceModifierPlugin.js";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin.js";

export interface ParameterDeclarationsConfiguration {
    strategy: "function" | "lambda",
    defaultValue?: string
    template: (parameters: string, signature: Signature) => string,
}

export interface ParameterDeclarationConfiguration {
    strategy: "function" | "lambda",
    defaultValue?: string
    inheritanceModifier?: string
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
    const {strategy, defaultValue, template} = configuration
    const initialSignature = extractSignature(node)

    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    if (strategy === "function") {
        const annotationService = context.lookupService<AnnotationService>(annotationServiceKey)
        const annotations = annotationService?.resolveAnnotations(node, context) ?? []
        const delimiter = `\n\n${annotations.join("\n")}`

        const inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)
        const signatures = expandUnions(initialSignature, context)

        return signatures
            .map(signature => {
                const inheritanceModifier = inheritanceModifierService?.resolveSignatureInheritanceModifier(node, signature, context) ?? undefined

                const parameters = signature
                    .filter(({parameter}) => !isThisParameter(parameter))
                    .map(({parameter, type, nullable}) => {
                        return convertParameterDeclarationWithFixedType(parameter, context, render, {
                            strategy,
                            defaultValue,
                            inheritanceModifier,
                            type,
                            nullable,
                        })
                    })
                    .filter(Boolean)
                    .join(", ")

                const prefix = signature[0] && isThisParameter(signature[0].parameter)
                    ? `/* ${typeScriptService?.printNode(signature[0].parameter)}, */ `
                    : ""

                return template(prefix + parameters, signature)
            })
            .join(delimiter)
    }

    if (strategy === "lambda") {
        const parameters = node.parameters
            .filter(parameter => !isThisParameter(parameter))
            .map(parameter => {
                return convertParameterDeclarationWithFixedType(parameter, context, render, {
                    strategy,
                    type: parameter.type,
                    nullable: false,
                })
            })
            .filter(Boolean)
            .join(", ")

        const prefix = node.parameters[0] && isThisParameter(node.parameters[0])
            ? `/* ${typeScriptService?.printNode(node.parameters[0])}, */ `
            : ""

        return template(prefix + parameters, initialSignature)
    }

    throw new Error(`Unknown parameter declaration strategy: ${strategy}`)
}

export const convertParameterDeclarationWithFixedType = (
    node: ParameterDeclaration,
    context: ConverterContext,
    render: Render,
    configuration: ParameterDeclarationConfiguration,
) => {
    const {type, strategy, inheritanceModifier} = configuration

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

    const defaultValue = configuration.defaultValue ?? "definedExternally"

    const isDefinedExternally = strategy === "function"
        && Boolean(node.questionToken)
        && inheritanceModifier !== "override"
        && defaultValue !== ""

    let renderedType = renderNullable(type, isOptional || configuration.nullable, context, render)

    if (type && node.dotDotDotToken) {
        if (renderedType.startsWith("Array")) {
            renderedType = renderedType.replace(/^Array<(.+)>$/, "$1")
        } else if (renderedType.startsWith("ReadonlyArray")) {
            renderedType = renderedType.replace(/^ReadonlyArray<(.+)>$/, "$1")
        } else if (renderedType.startsWith("js.array.ReadonlyArray")) {
            renderedType = renderedType.replace(/^js.array.ReadonlyArray<(.+)>$/, "$1")
        } else {
            renderedType = `Any? /* ${renderedType} */`
        }
    }


    return `${node.dotDotDotToken ? "vararg " : ""}${name}: ${renderedType}${isOptional ? " /* use undefined for default */" : ""}${isDefinedExternally ? ` = ${defaultValue}` : ""}`
}

const isThisParameter = (parameter: ParameterDeclaration) => (
    ts.isIdentifier(parameter.name)
    && parameter.name.text === "this"
)

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

            if (isThisParameter(parameter)) continue

            if (type && isNullableStringUnionType(type, context)) continue

            if (type && ts.isUnionTypeNode(type)) {
                checkCoverageService?.cover(type)

                const generatedSignatures: Signature[] = []

                const nullable = isNullableUnionType(type, context)

                const types = flatUnionTypes(type, context)

                for (const subtype of types) {
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

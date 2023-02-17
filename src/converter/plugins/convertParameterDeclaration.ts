import ts, {ParameterDeclaration, SignatureDeclarationBase, TypeNode} from "typescript";
import {createSimplePlugin} from "../plugin";
import {CheckCoverageService, checkCoverageServiceKey} from "./CheckCoveragePlugin";
import {TypeScriptService, typeScriptServiceKey} from "./TypeScriptPlugin";
import {isNullableType, isNullableUnionType, isPossiblyNullableType} from "./NullableUnionTypePlugin";
import {ConverterContext} from "../context";
import {Render} from "../render";

export interface ParameterDeclarationsConfiguration {
    strategy: "function" | "lambda",
    template: (parameters: string) => string,
}

export interface ParameterDeclarationConfiguration {
    strategy: "function" | "lambda",
    type: TypeNode | undefined,
    nullable: boolean,
}

export interface ParameterInfo {
    parameter: ParameterDeclaration,
    type: TypeNode | undefined,
    nullable: boolean
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

    if (strategy === "function") {
        const signatures = prepareParameters(node, context)

        return signatures
            .map(signature => {
                const parameters = signature
                    .map(({ parameter, type, nullable}) => {
                        return convertParameterDeclarationWithFixedType(parameter, context, render, {
                            strategy,
                            type,
                            nullable,
                        });
                    })
                    .join(", ")

                return template(parameters)
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
                });
            })
            .join(", ")

        return template(parameters)
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
    const typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    checkCoverageService?.cover(node)
    node.dotDotDotToken && checkCoverageService?.cover(node.dotDotDotToken)
    node.questionToken && checkCoverageService?.cover(node.questionToken)

    const parameterIndex = node.parent.parameters.indexOf(node)
    let name = parameterIndex === 0
        ? "param"
        : `param${parameterIndex}`

    if (ts.isIdentifier(node.name)) {
        name = render(node.name)
    } else {
        checkCoverageService?.deepCover(node.name)
    }

    let renderedType = type && render(type)

    if (!renderedType) {
        // throw new Error(`${name} parameter declaration without type is unsupported`)
        renderedType = "Any? /* some expression */" // TODO: resolve types
    }

    if (node.dotDotDotToken) {
        if (renderedType.startsWith("Array")) {
            renderedType = renderedType.replace(/^Array<(.+)>$/, "$1")
        } else {
            throw new Error(`Unhandled vararg declaration for ${name} parameter`)
        }
    }

    const isDefinedExternally = node.questionToken && strategy !== "lambda"

    let nullable = configuration.nullable
    let forcedNullable = false

    // handle `typeof` case
    const resolvedType = node.type && typeScriptService?.resolveType(node.type)

    if (
        strategy === "lambda"
        && node.questionToken
        && resolvedType
        && !isPossiblyNullableType(resolvedType)
    ) {
        nullable = true
        forcedNullable = true
    }

    return `${node.dotDotDotToken ? "vararg " : ""}${name}: ${renderedType}${nullable ? "?" : ""}${forcedNullable ? " /* use undefined for default */" : ""}${isDefinedExternally ? " = definedExternally" : ""}`
}

const prepareParameters = (
    node: SignatureDeclarationBase,
    context: ConverterContext,
): Signature[] => {
    const currentSignatures = [node.parameters.map(it => ({
        parameter: it,
        type: it.type,
        nullable: false
    }))]

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)

    for (let parameterIndex = 0; parameterIndex < node.parameters.length; parameterIndex++) {
        for (let signatureIndex = 0; signatureIndex < currentSignatures.length; signatureIndex++) {
            const signature = currentSignatures[signatureIndex]
            const {parameter, type} = signature[parameterIndex]

            if (type && ts.isUnionTypeNode(type)) {
                checkCoverageService?.cover(type)

                const generatedSignatures: Signature[] = []

                const nullable = isNullableUnionType(type)

                for (const subtype of type.types) {
                    if (isNullableType(subtype)) {
                        checkCoverageService?.deepCover(type)
                        continue
                    }

                    const generatedSignature: Signature = [...signature]
                    const parameterInfo = {
                        parameter,
                        type: subtype,
                        nullable
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

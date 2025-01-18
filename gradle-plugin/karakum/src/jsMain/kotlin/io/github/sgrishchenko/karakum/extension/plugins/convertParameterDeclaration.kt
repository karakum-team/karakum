package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.ConverterContext
import io.github.sgrishchenko.karakum.extension.Render
import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import js.array.ReadonlyArray
import js.objects.JsPlainObject
import seskar.js.JsValue
import typescript.*

sealed external interface ParameterDeclarationStrategy {
    companion object {
        @JsValue("function")
        val function: ParameterDeclarationStrategy

        @JsValue("lambda")
        val lambda: ParameterDeclarationStrategy
    }
}

@JsPlainObject
external interface ParameterDeclarationsConfiguration {
    val strategy: ParameterDeclarationStrategy
    val defaultValue: String?
    val template: (parameters: String, signature: Signature) -> String
}

@JsPlainObject
external interface ParameterDeclarationConfiguration {
    val strategy: ParameterDeclarationStrategy
    val defaultValue: String?
    val inheritanceModifier: String?
    val type: TypeNode?
    val nullable: Boolean
}

@JsPlainObject
external interface ParameterInfo {
    val parameter: ParameterDeclaration
    val type: TypeNode?
    val nullable: Boolean
    val optional: Boolean
}

typealias Signature = ReadonlyArray<ParameterInfo>

//val convertParameterDeclaration = createSimplePlugin { node: Node, context, render ->
//    if (!isParameter(node)) return@createSimplePlugin null
//
//    return convertParameterDeclarationWithFixedType(node, context, render, ParameterDeclarationConfiguration(
//        strategy = if (isFunctionTypeNode(node.parent)) {
//            ParameterDeclarationStrategy.lambda
//        } else {
//            ParameterDeclarationStrategy.function
//        },
//        type = node.type,
//        nullable = false,
//    ))
//}

/*
fun convertParameterDeclarations (
    node: SignatureDeclarationBase,
    context: ConverterContext,
    render: Render<Node>,
    configuration: ParameterDeclarationsConfiguration,
) {
    val strategy = configuration.strategy
    val defaultValue = configuration.defaultValue
    val template = configuration.template
    val initialSignature = extractSignature(node)

    val commentService = context.lookupService<CommentService>(commentServiceKey)
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    if (strategy === "function") {
        const annotationService = context.lookupService<AnnotationService>(annotationServiceKey)
        const annotations = annotationService?.resolveAnnotations(node, context) ?? []
        const leadingComment = commentService?.renderLeadingComments(node) ?? ""
        const delimiter = `\n\n${leadingComment}${annotations.join("\n")}`

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
}*/

/*export const convertParameterDeclarationWithFixedType = (
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
)*/

fun extractSignature(node: SignatureDeclarationBase) =
    node.parameters.asArray().map {
        ParameterInfo(
            parameter = it,
            type = it.type,
            nullable = false,
            optional = it.questionToken == null
        )
    }

/*
const expandUnions = (
    signature: Signature,
    context: ConverterContext,
): Signature[] => {
    const currentSignatures = [signature]

    const checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)

    for (let parameterIndex = 0; parameterIndex < signature.length; parameterIndex++) {
        for (let signatureIndex = 0; signatureIndex < currentSignatures.length; signatureIndex++) {
            const signature = currentSignatures[signatureIndex]

            if (!signature[parameterIndex]) continue

            const {parameter, type, optional} = signature[parameterIndex]
            const previousOptional = signature[parameterIndex - 1]?.optional ?? false

            if (isThisParameter(parameter)) continue

            if (type && isNullableLiteralUnionType(type, context)) continue

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

                if (
                    generatedSignatures.length > 1
                    && optional
                    && !previousOptional
                ) {
                    const strippedSignature = signature.filter(it => !it.optional)
                    generatedSignatures.unshift(strippedSignature)
                }

                currentSignatures.splice(signatureIndex, 1, ...generatedSignatures)
            }
        }
    }

    return currentSignatures
}*/

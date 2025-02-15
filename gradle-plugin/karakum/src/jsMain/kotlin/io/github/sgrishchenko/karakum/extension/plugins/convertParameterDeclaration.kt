package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.ConverterContext
import io.github.sgrishchenko.karakum.extension.Render
import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import io.github.sgrishchenko.karakum.extension.plugins.ParameterDeclarationStrategy.Companion.function
import io.github.sgrishchenko.karakum.extension.plugins.ParameterDeclarationStrategy.Companion.lambda
import io.github.sgrishchenko.karakum.extension.renderNullable
import io.github.sgrishchenko.karakum.util.escapeIdentifier
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

// TODO: ticket for JsPlainObject
//@OptIn(ExperimentalJsExport::class)
//@JsExport
@JsPlainObject
external interface ParameterDeclarationsConfiguration {
    val strategy: ParameterDeclarationStrategy
    val defaultValue: String?
    val template: (parameters: String, signature: Signature) -> String
}

// TODO: ticket for JsPlainObject
//@OptIn(ExperimentalJsExport::class)
//@JsExport
@JsPlainObject
external interface ParameterDeclarationConfiguration {
    val strategy: ParameterDeclarationStrategy
    val defaultValue: String?
    val inheritanceModifier: String?
    val type: TypeNode?
    val nullable: Boolean
}

// TODO: ticket for JsPlainObject
//@OptIn(ExperimentalJsExport::class)
//@JsExport
@JsPlainObject
external interface ParameterInfo {
    val parameter: ParameterDeclaration
    val type: TypeNode?
    val nullable: Boolean
    val optional: Boolean
}

typealias Signature = ReadonlyArray<ParameterInfo>

val convertParameterDeclaration = createSimplePlugin plugin@{ node: Node, context, render ->
    if (!isParameter(node)) return@plugin null

    convertParameterDeclarationWithFixedType(node, context, render, ParameterDeclarationConfiguration(
        strategy = if (isFunctionTypeNode(node.parent)) lambda else function,
        type = node.type,
        nullable = false,
    ))
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun convertParameterDeclarations(
    node: SignatureDeclarationBase,
    context: ConverterContext,
    render: Render<Node>,
    configuration: ParameterDeclarationsConfiguration,
): String {
    val strategy = configuration.strategy
    val defaultValue = configuration.defaultValue
    val template = configuration.template
    val initialSignature = extractSignature(node)

    val commentService = context.lookupService<CommentService>(commentServiceKey)
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    if (strategy == function) {
        val annotationService = context.lookupService<AnnotationService>(annotationServiceKey)
        val annotations = annotationService?.resolveAnnotations(node, context) ?: emptyArray()
        val leadingComment = commentService?.renderLeadingComments(node) ?: ""
        val delimiter = "\n\n${leadingComment}${annotations.joinToString(separator = "\n")}"

        val inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)
        val signatures = expandUnions(initialSignature, context)

        return signatures.joinToString(separator = delimiter) { signature ->
            val inheritanceModifier = inheritanceModifierService?.resolveSignatureInheritanceModifier(node, signature, context)

            val parameters = signature
                .filter { !isThisParameter(it.parameter) }
                .map {
                    convertParameterDeclarationWithFixedType(
                        it.parameter, context, render, ParameterDeclarationConfiguration(
                            strategy = strategy,
                            defaultValue = defaultValue,
                            inheritanceModifier = inheritanceModifier,
                            type = it.type,
                            nullable = it.nullable,
                        )
                    )
                }
                .filter { it.isNotEmpty() }
                .joinToString(separator = ", ")

            val firstParameter = signature.getOrNull(0)?.parameter
            val prefix = if (firstParameter != null && isThisParameter(firstParameter)) {
                "/* ${typeScriptService?.printNode(firstParameter)}, */ "
            } else {
                ""
            }

            template(prefix + parameters, signature)
        }
    }

    if (strategy == lambda) {
        val parameters = node.parameters.asArray()
            .filter { parameter -> !isThisParameter(parameter) }
            .map { parameter ->
                convertParameterDeclarationWithFixedType(parameter, context, render, ParameterDeclarationConfiguration(
                    strategy = strategy,
                    type = parameter.type,
                    nullable = false,
                ))
            }
            .filter { it.isNotEmpty() }
            .joinToString(separator = ", ")

        val firstParameter = node.parameters.asArray().getOrNull(0)
        val prefix = if (firstParameter != null && isThisParameter(firstParameter)) {
            "/* ${typeScriptService?.printNode(firstParameter)}, */ "
        } else {
            ""
        }

        return template(prefix + parameters, initialSignature)
    }

    error("Unknown parameter declaration strategy: $strategy")
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun convertParameterDeclarationWithFixedType(
    node: ParameterDeclaration,
    context: ConverterContext,
    render: Render<Node>,
    configuration: ParameterDeclarationConfiguration,
): String {
    val type = configuration.type
    val strategy = configuration.strategy
    val inheritanceModifier = configuration.inheritanceModifier

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)

    checkCoverageService?.cover(node)
    node.dotDotDotToken?.let { checkCoverageService?.cover(it) }
    node.questionToken?.let { checkCoverageService?.cover(it) }

    val name = if (isIdentifier(node.name)) {
        escapeIdentifier(render(node.name))
    } else {
        checkCoverageService?.deepCover(node.name)

        @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
        val signature = (node.parent as SignatureDeclarationBase)

        val parameterIndex = signature.parameters.asArray().indexOf(node)

        val anonymousParameters = signature.parameters.asArray()
            .filter { parameter -> !isIdentifier(parameter.name) }

        if (anonymousParameters.size == 1) "options" else "param${parameterIndex}"
    }

    val isOptional = strategy == lambda && node.questionToken != null

    val defaultValue = configuration.defaultValue ?: "definedExternally"

    val isDefinedExternally = strategy == function
        && node.questionToken != null
        && inheritanceModifier !== "override"
        && defaultValue !== ""

    var renderedType = renderNullable(type, isOptional || configuration.nullable, context, render)

    if (type != null && node.dotDotDotToken != null) {
        renderedType = if (renderedType.startsWith("Array")) {
            renderedType.replace("^Array<(.+)>$".toRegex(), "$1")
        } else if (renderedType.startsWith("ReadonlyArray")) {
            renderedType.replace("^ReadonlyArray<(.+)>$".toRegex(), "$1")
        } else if (renderedType.startsWith("js.array.ReadonlyArray")) {
            renderedType.replace("^js.array.ReadonlyArray<(.+)>$".toRegex(), "$1")
        } else {
            "Any? /* $renderedType */"
        }
    }

    return "${if (node.dotDotDotToken != null) "vararg " else ""}${name}: ${renderedType}${if (isOptional) " /* use undefined for default */" else ""}${if (isDefinedExternally) " = $defaultValue" else ""}"
}

private fun isThisParameter(parameter: ParameterDeclaration): Boolean {
    val name = parameter.name

    return isIdentifier(name) && name.text === "this"
}

private fun extractSignature(node: SignatureDeclarationBase): Signature =
    node.parameters.asArray().map {
        ParameterInfo(
            parameter = it,
            type = it.type,
            nullable = false,
            optional = it.questionToken != null
        )
    }.toTypedArray()

private fun expandUnions(
    initialSignature: Signature,
    context: ConverterContext,
): ReadonlyArray<Signature> {
    val currentSignatures = mutableListOf(initialSignature)

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)

    for (parameterIndex in initialSignature.indices) {
        var signatureIndex = 0
        while (signatureIndex < currentSignatures.size) {
            val signature = currentSignatures[signatureIndex]
            val parameterInfo = signature.getOrNull(parameterIndex)
            if (parameterInfo == null) {
                signatureIndex++
                continue
            }

            val parameter = parameterInfo.parameter
            val type = parameterInfo.type
            val optional = parameterInfo.optional
            val previousOptional = signature.getOrNull(parameterIndex - 1)?.optional ?: false

            if (isThisParameter(parameter)) {
                signatureIndex++
                continue
            }

            if (type != null && isNullableLiteralUnionType(type, context)) {
                signatureIndex++
                continue
            }

            if (type != null && isUnionTypeNode(type)) {
                checkCoverageService?.cover(type)

                val generatedSignatures = mutableListOf<Signature>()

                val nullable = isNullableUnionType(type, context)

                val types = flatUnionTypes(type, context)

                for (subtype in types) {
                    if (isNullableType(subtype)) {
                        checkCoverageService?.deepCover(type)
                        continue
                    }

                    val generatedSignature = signature.toMutableList()
                    val parameterInfo = ParameterInfo(
                        parameter = parameter,
                        type = subtype,
                        nullable = nullable,
                        optional = optional,
                    )
                    generatedSignature.removeAt(parameterIndex)
                    generatedSignature.add(parameterIndex, parameterInfo)
                    generatedSignatures += generatedSignature.toTypedArray()
                }

                if (
                    generatedSignatures.size > 1
                    && optional
                    && !previousOptional
                ) {
                    val strippedSignature = signature.filter { !it.optional }.toTypedArray()
                    generatedSignatures.add(0, strippedSignature)
                }

                currentSignatures.removeAt(signatureIndex)
                currentSignatures.addAll(signatureIndex, generatedSignatures)
            }
            signatureIndex++
        }
    }

    return currentSignatures.toTypedArray()
}

package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import io.github.sgrishchenko.karakum.extension.ifPresent
import io.github.sgrishchenko.karakum.util.escapeIdentifier
import typescript.asArray
import typescript.isMethodSignature

val convertMethodSignature = createSimplePlugin plugin@{ node, context, render ->
    if (!isMethodSignature(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)

    checkCoverageService?.cover(node)

    val inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)

    val name = escapeIdentifier(render(node.name))
    val annotation = createKebabAnnotation(node.name)

    val typeParameters = node.typeParameters?.asArray()
        ?.map { render(it) }
        ?.filter { it.isNotEmpty() }
        ?.joinToString(separator = ", ")

    val returnType = node.type?.let{ render(it) }

    if (node.questionToken != null) {
        return@plugin convertParameterDeclarations(node, context, render, ParameterDeclarationsConfiguration(
            strategy = ParameterDeclarationStrategy.lambda,
            template = { parameters, signature ->
                val inheritanceModifier = inheritanceModifierService?.resolveSignatureInheritanceModifier(node, signature, context)

                val functionType = if (node.typeParameters != null) {
                    "Function<Any?> /* ${typeScriptService?.printNode(node)} */"
                } else if (node.parameters.asArray().any { parameter -> parameter.dotDotDotToken != null }) {
                    "Function<${returnType}> /* ${typeScriptService?.printNode(node)} */"
                } else {
                    "(${parameters}) -> ${returnType ?: "Any?"}"
                }

                "${ifPresent(annotation) { "${it}\n" }}${ifPresent(inheritanceModifier) { "$it "}}val ${name}: (${functionType})?"
            }
        ))
    }

    convertParameterDeclarations(node, context, render, ParameterDeclarationsConfiguration(
        strategy = ParameterDeclarationStrategy.function,
        template = { parameters, signature ->
            val inheritanceModifier = inheritanceModifierService?.resolveSignatureInheritanceModifier(node, signature, context)

            "${ifPresent(annotation) { "${it}\n" }}${ifPresent(inheritanceModifier) { "$it "}}fun ${ifPresent(typeParameters) { "<${it}> " }}${name}(${parameters})${ifPresent(returnType) { ": $it" }}"
        }
    ))
}

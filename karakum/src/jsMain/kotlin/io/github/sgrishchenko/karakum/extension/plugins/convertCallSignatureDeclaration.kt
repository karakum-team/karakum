package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import io.github.sgrishchenko.karakum.extension.ifPresent
import typescript.asArray
import typescript.isCallSignatureDeclaration

val convertCallSignatureDeclaration = createSimplePlugin plugin@{ node, context, render ->
    if (!isCallSignatureDeclaration(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)

    checkCoverageService?.cover(node)

    val inheritanceModifierService = context.lookupService<InheritanceModifierService>(inheritanceModifierServiceKey)

    val typeParameters = node.typeParameters
        ?.asArray()
        ?.map { render(it) }
        ?.filter { it.isNotEmpty() }
        ?.joinToString(separator = ", ")

    val returnType = node.type?.let { render(it) }

    convertParameterDeclarations(node, context, render, ParameterDeclarationsConfiguration(
        strategy = ParameterDeclarationStrategy.function,
        template = { parameters, signature ->
            val inheritanceModifier = inheritanceModifierService?.resolveSignatureInheritanceModifier(node, signature, context)

            """
@seskar.js.JsNativeInvoke
${ifPresent(inheritanceModifier) { "$it "}}operator fun ${ifPresent(typeParameters) { "<${it}>" }} invoke(${parameters})${ifPresent(returnType) { ": $it" }}
            """.trim()
        }
    ))
}

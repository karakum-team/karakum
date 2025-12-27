package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.configuration.`object`
import io.github.sgrishchenko.karakum.extension.createPlugin
import io.github.sgrishchenko.karakum.extension.ifPresent
import typescript.SyntaxKind
import typescript.asArray
import typescript.isFunctionDeclaration

val convertFunctionDeclaration = createPlugin plugin@{ node, context, render ->
    if (!isFunctionDeclaration(node)) return@plugin null

    val checkCoverageService = context.lookupService(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    val exportModifier = node.modifiers?.asArray()?.find { it.kind == SyntaxKind.ExportKeyword }
    exportModifier?.let { checkCoverageService?.cover(it) }

    val declareModifier = node.modifiers?.asArray()?.find { it.kind == SyntaxKind.DeclareKeyword }
    declareModifier?.let { checkCoverageService?.cover(it) }

    // skip body
    node.body?.let { checkCoverageService?.deepCover(it) }

    val typeScriptService = context.lookupService(typeScriptServiceKey)
    val namespaceInfoService = context.lookupService(namespaceInfoServiceKey)

    val name = node.name?.let { render(it) } ?: "Anonymous"

    val namespace = typeScriptService?.findClosestNamespace(node)

    var externalModifier = "external "

    if (namespace != null && namespaceInfoService?.resolveNamespaceStrategy(namespace) == NamespaceStrategy.`object`) {
        externalModifier = ""
    }

    val typeParameters = node.typeParameters?.asArray()
        ?.map{ render(it) }
        ?.filter { it.isNotEmpty() }
        ?.joinToString(separator =", ")

    val returnType = node.type?.let { render(it) }

    convertParameterDeclarations(node, context, render, ParameterDeclarationsConfiguration(
        strategy = ParameterDeclarationStrategy.function,
        template = { parameters, _ ->
            "${externalModifier}fun ${ifPresent(typeParameters) { "<${it}> "}}${name}(${parameters})${ifPresent(returnType) { ": $it"}}"
        }
    ))
}

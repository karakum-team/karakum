package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.configuration.NamespaceStrategy
import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import io.github.sgrishchenko.karakum.extension.ifPresent
import typescript.Node
import typescript.SyntaxKind
import typescript.asArray
import typescript.isFunctionDeclaration

val convertFunctionDeclaration = createSimplePlugin plugin@{ node: Node, context, render ->
    if (!isFunctionDeclaration(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    val exportModifier = node.modifiers?.asArray()?.find { it.kind == SyntaxKind.ExportKeyword }
    exportModifier?.let { checkCoverageService?.cover(it) }

    val declareModifier = node.modifiers?.asArray()?.find { it.kind == SyntaxKind.DeclareKeyword }
    declareModifier?.let { checkCoverageService?.cover(it) }

    // skip body
    node.body?.let { checkCoverageService?.deepCover(it) }

    val typeScriptService = context.lookupService<TypeScriptService>(typeScriptServiceKey)
    val namespaceInfoService = context.lookupService<NamespaceInfoService>(namespaceInfoServiceKey)

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

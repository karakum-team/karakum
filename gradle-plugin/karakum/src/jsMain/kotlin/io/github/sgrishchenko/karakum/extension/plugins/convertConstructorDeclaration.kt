package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createSimplePlugin
import typescript.Node
import typescript.isConstructorDeclaration

val convertConstructorDeclaration = createSimplePlugin plugin@{ node: Node, context, render ->
    if (!isConstructorDeclaration(node)) return@plugin null

    val checkCoverageService = context.lookupService<CheckCoverageService>(checkCoverageServiceKey)
    checkCoverageService?.cover(node)

    convertParameterDeclarations(node, context, render, ParameterDeclarationsConfiguration(
        strategy = ParameterDeclarationStrategy.function,
        template = { parameters, _ ->
            "constructor (${parameters})"
        }
    ))
}

package io.github.sgrishchenko.karakum.extension.plugins

import io.github.sgrishchenko.karakum.extension.createPlugin
import typescript.isConstructorDeclaration

val convertConstructorDeclaration = createPlugin plugin@{ node, context, render ->
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
